/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.agon.core.guice;

import com.google.inject.*;
import com.google.inject.spi.BindingScopingVisitor;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.core.spi.component.ioc.*;

import javax.ws.rs.WebApplicationException;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Guice-based {@link com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory}.
 *
 * @author Gili Tzabari
 * @author Paul Sandoz
 * @author Charlie Groves
 */
public class GuiceComponentProviderFactory implements IoCComponentProviderFactory {
    private static final Logger LOGGER =
            Logger.getLogger(GuiceComponentProviderFactory.class.getName());
    private final Map<Scope, ComponentScope> scopeMap = createScopeMap();
    private final Injector injector;

    /**
     * Creates a new GuiceComponentProviderFactory.
     *
     * @param config   the resource configuration
     * @param injector the Guice injector
     */
    public GuiceComponentProviderFactory(ResourceConfig config, Injector injector) {
        if (injector == null) {
            throw new NullPointerException("Guice Injector can not be null!");
        }
        this.injector = injector;
        register(config, injector);
    }

    /**
     * Registers any Guice-bound providers or root resources.
     *
     * @param config   the resource configuration
     * @param injector the Guice injector
     */
    private void register(ResourceConfig config, Injector injector) {
        while (injector != null) {
            for (Key<?> key : injector.getBindings().keySet()) {
                Type type = key.getTypeLiteral().getType();
                if (type instanceof Class) {
                    Class<?> c = (Class) type;
                    if (ResourceConfig.isProviderClass(c)) {
                        LOGGER.log(Level.INFO, "Registering {0} as a provider class", c.getName());
                        config.getClasses().add(c);
                    } else if (ResourceConfig.isRootResourceClass(c)) {
                        LOGGER.log(Level.INFO, "Registering {0} as a root resource class", c.getName());
                        config.getClasses().add(c);
                    }
                }
            }
            injector = injector.getParent();
        }
    }

    @Override
    public IoCComponentProvider getComponentProvider(Class<?> c) {
        return getComponentProvider(null, c);
    }

    @Override
    public IoCComponentProvider getComponentProvider(ComponentContext cc, Class<?> clazz) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "getComponentProvider({0})", clazz.getName());
        }
        Key<?> key = Key.get(clazz);
        Injector i = findInjector(key);
        // If there is no explicit binding
        if (i == null) {
            // If @Inject is explicitly declared on constructor
            if (isGuiceConstructorInjected(clazz)) {
                try {
                    // If a binding is possible
                    if (injector.getBinding(key) != null) {
                        LOGGER.log(Level.INFO, "Binding {0} to GuiceInstantiatedComponentProvider", clazz.getName());
                        return new GuiceInstantiatedComponentProvider(injector, clazz);
                    }
                } catch (ConfigurationException e) {
                    // The class cannot be injected.
                    // For example, the constructor might contain parameters that
                    // cannot be injected
                    LOGGER.log(Level.SEVERE, "Cannot bind " + clazz.getName(), e);
                    // Guice should have picked this up. We fail-fast to prevent
                    // Jersey from trying to handle injection.
                    throw e;
                }
                // If @Inject is declared on field or method
            } else if (isGuiceFieldOrMethodInjected(clazz)) {
                LOGGER.log(Level.INFO, "Binding {0} to GuiceInjectedComponentProvider", clazz.getName());
                return new GuiceInjectedComponentProvider(injector);
            } else {
                return null;
            }
        }
        ComponentScope componentScope = getComponentScope(key, i);
        LOGGER.log(Level.INFO, "Binding {0} to GuiceManagedComponentProvider with the scope \"{1}\"",
                new Object[]{clazz.getName(), componentScope});
        return new GuiceManagedComponentProvider(i, componentScope, clazz);
    }

    private ComponentScope getComponentScope(Key<?> key, Injector i) {
        return i.getBinding(key).acceptScopingVisitor(new BindingScopingVisitor<ComponentScope>() {
            @Override
            public ComponentScope visitEagerSingleton() {
                return ComponentScope.Singleton;
            }

            @Override
            public ComponentScope visitScope(Scope theScope) {
                ComponentScope cs = scopeMap.get(theScope);
                return (cs != null) ? cs : ComponentScope.Undefined;
            }

            @Override
            public ComponentScope visitScopeAnnotation(Class scopeAnnotation) {
                // This method is not invoked for Injector bindings
                throw new UnsupportedOperationException();
            }

            @Override
            public ComponentScope visitNoScoping() {
                return ComponentScope.PerRequest;
            }
        });
    }

    private Injector findInjector(Key<?> key) {
        Injector i = injector;
        while (i != null) {
            if (i.getBindings().containsKey(key)) {
                return i;
            }
            i = i.getParent();
        }
        return null;
    }

    /**
     * Determine if a class is an implicit Guice component that can be
     * instantiated by Guice and the life-cycle managed by Jersey.
     *
     * @param c the class.
     * @return true if the class is an implicit Guice component.
     * @deprecated see {@link #isGuiceConstructorInjected(java.lang.Class) }
     */
    @Deprecated
    public boolean isImplicitGuiceComponent(Class<?> c) {
        return isGuiceConstructorInjected(c);
    }

    /**
     * Determine if a class is an implicit Guice component that can be
     * instantiated by Guice and the life-cycle managed by Jersey.
     *
     * @param c the class.
     * @return true if the class is an implicit Guice component.
     */
    public boolean isGuiceConstructorInjected(Class<?> c) {
        for (Constructor<?> con : c.getDeclaredConstructors()) {
            if (isInjectable(con)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine if a class uses field or method injection via Guice
     * using the {@code Inject} annotation
     *
     * @param c the class.
     * @return true if the class is an implicit Guice component.
     */
    public boolean isGuiceFieldOrMethodInjected(Class<?> c) {
        for (Method m : c.getDeclaredMethods()) {
            if (isInjectable(m)) {
                return true;
            }
        }
        for (Field f : c.getDeclaredFields()) {
            if (isInjectable(f)) {
                return true;
            }
        }
        return !c.equals(Object.class) && isGuiceFieldOrMethodInjected(c.getSuperclass());
    }

    private static boolean isInjectable(AnnotatedElement element) {
        return (element.isAnnotationPresent(com.google.inject.Inject.class)
                || element.isAnnotationPresent(javax.inject.Inject.class));
    }

    /**
     * Maps a Guice scope to a Jersey scope.
     *
     * @return the map
     */
    public Map<Scope, ComponentScope> createScopeMap() {
        Map<Scope, ComponentScope> result = new HashMap<Scope, ComponentScope>();
        result.put(Scopes.SINGLETON, ComponentScope.Singleton);
        result.put(Scopes.NO_SCOPE, ComponentScope.PerRequest);
        return result;
    }

    private static class GuiceInjectedComponentProvider
            implements IoCProxiedComponentProvider {
        private final Injector injector;

        public GuiceInjectedComponentProvider(Injector injector) {
            this.injector = injector;
        }

        @Override
        public Object getInstance() {
            throw new IllegalStateException();
        }

        @Override
        public Object proxy(Object o) {
            try {
                injector.injectMembers(o);
            } catch (ProvisionException e) {
                if (e.getCause() instanceof WebApplicationException) {
                    throw (WebApplicationException) e.getCause();
                }
                throw e;
            }
            return o;
        }
    }

    /**
     * Guice injects instances while Jersey manages their scope.
     *
     * @author Gili Tzabari
     */
    private static class GuiceInstantiatedComponentProvider
            implements IoCInstantiatedComponentProvider {
        private final Injector injector;
        private final Class<?> clazz;

        /**
         * Creates a new GuiceManagedComponentProvider.
         *
         * @param injector the injector
         * @param clazz    the class
         */
        public GuiceInstantiatedComponentProvider(Injector injector, Class<?> clazz) {
            this.injector = injector;
            this.clazz = clazz;
        }

        public Class<?> getInjectableClass(Class<?> c) {
            return c.getSuperclass();
        }

        // IoCInstantiatedComponentProvider
        @Override
        public Object getInjectableInstance(Object o) {
            return o;
        }

        @Override
        public Object getInstance() {
            try {
                return injector.getInstance(clazz);
            } catch (ProvisionException e) {
                if (e.getCause() instanceof WebApplicationException) {
                    throw (WebApplicationException) e.getCause();
                }
                throw e;
            }
        }
    }

    /**
     * Guice injects instances and manages their scope.
     *
     * @author Gili Tzabari
     */
    private static class GuiceManagedComponentProvider extends GuiceInstantiatedComponentProvider
            implements IoCManagedComponentProvider {
        private final ComponentScope scope;

        /**
         * Creates a new GuiceManagedComponentProvider.
         *
         * @param injector the injector
         * @param scope    the Jersey scope
         * @param clazz    the class
         */
        public GuiceManagedComponentProvider(Injector injector, ComponentScope scope, Class<?> clazz) {
            super(injector, clazz);
            this.scope = scope;
        }

        @Override
        public ComponentScope getScope() {
            return scope;
        }
    }
}
