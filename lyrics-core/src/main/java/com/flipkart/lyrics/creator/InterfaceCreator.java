/*
 * Copyright 2017 Flipkart Internet, pvt ltd.
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

package com.flipkart.lyrics.creator;

import com.flipkart.lyrics.model.TypeModel;
import com.flipkart.lyrics.sets.HandlerSet;
import com.flipkart.lyrics.specs.TypeSpec;

/**
 * Created by shrey.garg on 23/01/17.
 */
public class InterfaceCreator extends TypeCreator {

    @Override
    public TypeSpec.Builder process(HandlerSet handlerSet, TypeModel typeModel) {
        TypeSpec.Builder typeBuilder = TypeSpec.interfaceBuilder(handlerSet.getMetaInfo().getClassName());

        handlerSet.getTypeAnnotationHandler().process(typeBuilder, typeModel);
        handlerSet.getGenericsHandler().process(typeBuilder, typeModel);
        handlerSet.getModifiersHandler().process(typeBuilder, typeModel);
        handlerSet.getInterfacesHandler().process(typeBuilder, typeModel);
        handlerSet.getInterfaceMethodsHandler().process(typeBuilder, typeModel);

        handlerSet.getRuleSet().getGlobalDeprecatedRule().process(typeBuilder, typeModel);

        handleAdditionalProperties(handlerSet.getTune(), typeBuilder, typeModel);

        return typeBuilder;
    }
}
