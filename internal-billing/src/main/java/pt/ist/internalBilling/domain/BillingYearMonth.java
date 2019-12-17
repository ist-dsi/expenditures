package pt.ist.internalBilling.domain;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;

public class BillingYearMonth extends BillingYearMonth_Base {
    
    protected BillingYearMonth() {
        setInternalBillingService(InternalBillingService.getInstance());
    }

    BillingYearMonth(int year,int month) {
        setInternalBillingService(InternalBillingService.getInstance());
        setYear(year);
        setMonth(month);
        setYearMonthStatus(YearMonthStatus.CLOSING);
    }
    
   
    public static boolean isAuthorized() {
        final User user = Authenticate.getUser();
        return DynamicGroup.get("billingservicesmanager").isMember(user);
    }
    
    
    public  boolean isClosed() {
       return this.getYearMonthStatus()==YearMonthStatus.CLOSED; 
    }
    @Atomic
    public static void create(final int year, final int month) {
        new BillingYearMonth(year,month); 
        
    }
    
    @Atomic
    public static void close(BillingYearMonth billingYearMonth) {
        billingYearMonth.setYearMonthStatus(YearMonthStatus.CLOSED);
        
    }
    
    public  boolean hasBillingOnYearMonth(int year, int month) {
        return this.getYear()==year && this.getMonth()==month?true:false;
    }
    

    
}
