package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

public class VariantBean implements Serializable {
    public static enum Type {
	INTEGER, STRING
    };

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private Object value;
    private Type type;

    public VariantBean() {
	value = null;
    }

    public Type getType() {
	return type;
    }

    protected void setType(Type type) {
	this.type = type;
    }

    public Integer getInteger() {
	return (Integer) (isType(Type.INTEGER) ? this.value : null); 
    }

    public void setInteger(Integer value) {
	this.value = value;
	setType(Type.INTEGER);
    }

    public String getString() {
	return (String) (isType(Type.STRING) ? this.value : null);
    }

    public void setString(String string) {
	this.value = string;
	setType(Type.STRING);
    }

    private boolean isType(Type type) {
	return type.equals(this.getType());
    }
}
