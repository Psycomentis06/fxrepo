package com.github.psycomentis06.fxrepomain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ExceptionViolationModel extends ExceptionModel{
    HashMap<String, String> constraints;
}
