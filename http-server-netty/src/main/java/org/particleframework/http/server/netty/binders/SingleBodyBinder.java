/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.particleframework.http.server.netty.binders;

import io.reactivex.Single;
import org.particleframework.context.BeanLocator;
import org.particleframework.context.annotation.Requires;
import org.particleframework.core.convert.ArgumentConversionContext;
import org.particleframework.core.convert.ConversionContext;
import org.particleframework.core.convert.ConversionService;
import org.particleframework.core.type.Argument;
import org.particleframework.http.HttpRequest;
import org.particleframework.http.server.HttpServerConfiguration;
import org.particleframework.http.server.binding.binders.DefaultBodyAnnotationBinder;
import org.particleframework.http.server.binding.binders.NonBlockingBodyArgumentBinder;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Optional;

/**
 * Bindings {@link org.particleframework.http.annotation.Body} arguments of type {@link Single}
 *
 * @author Graeme Rocher
 * @since 1.0
 */
@Singleton
@Requires(classes = Single.class)
public class SingleBodyBinder extends DefaultBodyAnnotationBinder<Single> implements NonBlockingBodyArgumentBinder<Single> {

    public static final Argument<Single> TYPE = Argument.of(Single.class);

    private PublisherBodyBinder publisherBodyBinder;


    public SingleBodyBinder(ConversionService conversionService, BeanLocator beanLocator, HttpServerConfiguration httpServerConfiguration) {
        super(conversionService);
        this.publisherBodyBinder = new PublisherBodyBinder(conversionService, beanLocator, httpServerConfiguration);
    }

    @Override
    public Argument<Single> argumentType() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BindingResult<Single> bind(ArgumentConversionContext<Single> context, HttpRequest<?> source) {
        Collection<Argument<?>> typeVariables = context.getArgument().getTypeVariables().values();

        BindingResult<Publisher> result = publisherBodyBinder.bind(
                ConversionContext.of(Argument.of(Publisher.class, (Argument[]) typeVariables.toArray(new Argument[typeVariables.size()]))),
                source
        );
        if(result.isPresentAndSatisfied()) {
            return () -> Optional.of(Single.fromPublisher(result.get()));
        }
        return BindingResult.EMPTY;
    }
}