package com.lineage.domain.valueobject;


public sealed interface PropertyValue
    permits StringValue, DateValue, ReferenceValue {
}