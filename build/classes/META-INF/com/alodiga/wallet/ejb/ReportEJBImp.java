package com.alodiga.wallet.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import org.apache.log4j.Logger;
import com.alodiga.wallet.common.ejb.ReportEJB;
import com.alodiga.wallet.common.ejb.ReportEJBLocal;
import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.genericEJB.AbstractWalletEJB;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
import com.alodiga.wallet.common.genericEJB.WalletContextInterceptor;
import com.alodiga.wallet.common.genericEJB.WalletLoggerInterceptor;
import com.alodiga.wallet.common.model.ParameterType;
import com.alodiga.wallet.common.model.Report;
import com.alodiga.wallet.common.model.ReportParameter;
import com.alodiga.wallet.common.model.ReportType;
import com.alodiga.wallet.common.model.Transaction;
import com.alodiga.wallet.common.model.User;
import com.alodiga.wallet.common.utils.EjbConstants;

@Interceptors({WalletLoggerInterceptor.class, WalletContextInterceptor.class})
@Stateless(name = EjbConstants.REPORT_EJB, mappedName = EjbConstants.REPORT_EJB)
@TransactionManagement(TransactionManagementType.BEAN)
public class ReportEJBImp extends AbstractWalletEJB implements ReportEJB, ReportEJBLocal {
    private static final Logger logger = Logger.getLogger(ReportEJBImp.class);
    
    //Report
    @Override
    public void deleteProfileReports(EJBRequest request) throws NullParameterException, GeneralException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteReportParameter(EJBRequest request) throws NullParameterException, GeneralException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Report enableProduct(EJBRequest request) throws GeneralException, NullParameterException, RegisterNotFoundException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<Report> getReport(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Report> getReportByReportTypeId(Long reportTypeId, User currentUser) throws NullParameterException, GeneralException, EmptyListException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Report loadReport(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<String> runReport(EJBRequest request) throws NullParameterException, GeneralException, EmptyListException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Report saveReport(EJBRequest request) throws NullParameterException, GeneralException, NullParameterException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //ParameterType
    @Override
    public List<ParameterType> getParameterType(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ParameterType loadParameterType(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //ReportType
    @Override
    public List<ReportType> getReportTypes(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //ReportParameter
    @Override
    public List<ReportParameter> getReportParameter(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ReportParameter loadReportParameter(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
