package com.mobiquityinc.mobit.modules.it.test_utils_dbunit.compare.utils;

import com.google.common.io.CharStreams;
import com.mobiquityinc.mobit.modules.generic.variables_resolver.VariablesResolver;
import com.mobiquityinc.mobit.modules.generic.variables_resolver.impl.NoOpVariablesResolver;
import com.mobiquityinc.mobit.modules.generic.variables_resolver.impl.escape.impl.ValueEscapers;
import lombok.NonNull;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class DbUnitXmlDataSetUtils {

    private DbUnitXmlDataSetUtils() {
        throw new UnsupportedOperationException(
                "it is forbidden to instantiate this utility class; just use the static methods"
        );
    }

    public static IDataSet loadFromResource(@NonNull final Resource resource) {
        return loadFromResource(resource, NoOpVariablesResolver.instance());
    }

    public static IDataSet loadFromResource(@NonNull final Resource resource,
                                            @NonNull final VariablesResolver variablesResolver) {
        try {
            return tryToLoadFromResource(resource, variablesResolver);
        } catch (final Exception e) {
            throw new DbUnitXmlDataSetUtilsException("failed to load data set from [" + resource + "]", e);
        }
    }

    private static IDataSet tryToLoadFromResource(@NonNull final Resource resource,
                                                  @NonNull final VariablesResolver variablesResolver)
            throws IOException, DataSetException {
        try (final InputStreamReader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            final String contentWithVariables = CharStreams.toString(reader);

            final String resolvedContent = variablesResolver.resolveVariablesInText(
                    contentWithVariables,
                    ValueEscapers.xml()
            );

            return new FlatXmlDataSetBuilder()
                    .setColumnSensing(true)
                    .build(
                            new StringReader(resolvedContent)
                    );
        }
    }

}
