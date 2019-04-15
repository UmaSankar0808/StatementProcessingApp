package com.xyzbank.customerstatementprocessor.transformation;

import java.nio.file.Path;
import java.util.List;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;

/**
 * FileUnmarshaller.java - All input file marshallers should implement this class.
 *
 */
@FunctionalInterface
public interface FileUnmarshaller {

    public List<StatementRecord> unmarshall(Path path);

}