package com.xyzbank.customerstatementprocessor.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.xyzbank.customerstatementprocessor.model.StatementRecords;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JaxbContextUtil {
    
    private static JAXBContext jaxbInstance;

    static {
        try {
            jaxbInstance = JAXBContext.newInstance(StatementRecords.class);
        } catch (JAXBException exp) {
            log.error(exp.getMessage(), exp);
        }
    }
    
    public static JAXBContext getInstance() {
        return jaxbInstance;
    }
 }
