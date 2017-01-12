/*
 * Copyright 2016 Flipkart Internet, pvt ltd.
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

package com.flipkart.lyrics;

import com.flipkart.lyrics.annotators.AnnotationStyle;
import com.flipkart.lyrics.annotators.validations.ValidationAnnotatorStyle;
import com.flipkart.lyrics.config.Tune;
import com.flipkart.lyrics.model.MetaInfo;
import com.flipkart.lyrics.model.TypeModel;
import com.flipkart.lyrics.sets.CreatorSet;
import com.flipkart.lyrics.sets.DefaultCreatorSet;
import com.flipkart.lyrics.sets.HandlerSet;
import com.flipkart.lyrics.sets.RuleSet;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.flipkart.lyrics.helper.Helper.*;

/**
 * Created by shrey.garg on 27/11/16.
 */
public class Song {
    private Tune tune;

    public Song(Tune tune) {
        this.tune = tune;
    }

    public void createType(String name, String fullPackage, TypeModel typeModel, File targetFolder) throws IOException {
        List<AnnotationStyle> annotationStyles = processAnnotationStyles(tune);
        List<ValidationAnnotatorStyle> validationAnnotatorStyles = processValidationAnnotationStyles(tune);
        MetaInfo metaInfo = new MetaInfo(name, fullPackage, getTypeVariables(typeModel.getGenericVariables()), annotationStyles, validationAnnotatorStyles);
        RuleSet ruleSet = processRuleSet(tune, metaInfo);
        HandlerSet handlerSet = processHandlerSet(tune, metaInfo, ruleSet);

        TypeSpec.Builder typeBuilder = getCreator(typeModel.getType(), tune.getCreatorSet()).process(handlerSet, typeModel);
        JavaFile javaFile = JavaFile.builder(fullPackage, typeBuilder.build())
                .indent("    ")
                .skipJavaLangImports(true)
                .build();
        javaFile.writeTo(targetFolder);
    }

    private HandlerSet processHandlerSet(Tune tune, MetaInfo metaInfo, RuleSet ruleSet) {
        HandlerSet handlerSet = tune.getHandlerSet();
        handlerSet.setMetaInfo(metaInfo);
        handlerSet.setRuleSet(ruleSet);
        handlerSet.setTune(tune);
        return handlerSet;
    }

    private RuleSet processRuleSet(Tune tune, MetaInfo metaInfo) {
        RuleSet ruleSet = tune.getRuleSet();
        ruleSet.setTune(tune);
        ruleSet.setMetaInfo(metaInfo);
        return ruleSet;
    }
}
