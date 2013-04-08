package module.mission.domain.util;

import java.io.Serializable;
import java.math.BigDecimal;

import module.mission.domain.DailyPersonelExpenseCategory;
import module.mission.domain.DailyPersonelExpenseTable;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.fenixWebFramework.services.Service;

public class DailyPersonelExpenseCategoryBean implements Serializable {

    private DailyPersonelExpenseTable dailyPersonelExpenseTable;
    private String description;
    private Money value;
    private BigDecimal minSalaryValue;

    public DailyPersonelExpenseCategoryBean(final DailyPersonelExpenseTable dailyPersonelExpenseTable) {
        this.dailyPersonelExpenseTable = dailyPersonelExpenseTable;
    }

    public DailyPersonelExpenseTable getDailyPersonelExpenseTable() {
        return dailyPersonelExpenseTable;
    }

    public void setDailyPersonelExpenseTable(DailyPersonelExpenseTable dailyPersonelExpenseTable) {
        this.dailyPersonelExpenseTable = dailyPersonelExpenseTable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    public BigDecimal getMinSalaryValue() {
        return minSalaryValue;
    }

    public void setMinSalaryValue(BigDecimal minSalaryValue) {
        this.minSalaryValue = minSalaryValue;
    }

    @Service
    public DailyPersonelExpenseCategory createDailyPersonelExpenseCategory() {
        return dailyPersonelExpenseTable.createDailyPersonelExpenseCategory(description, value, minSalaryValue);
    }

}
