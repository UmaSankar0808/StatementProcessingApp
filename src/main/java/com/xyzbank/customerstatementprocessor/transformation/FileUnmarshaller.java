package com.xyzbank.customerstatementprocessor.transformation;

import java.nio.file.Path;
import java.util.List;

import com.xyzbank.customerstatementprocessor.model.StatementRecord;

public interface FileUnmarshaller {

    public List<StatementRecord> unmarshall(Path path);
    
}
