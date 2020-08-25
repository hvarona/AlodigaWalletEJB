package com.alodiga.wallet.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import com.alodiga.wallet.common.ejb.BusinessEJB;
import com.alodiga.wallet.common.genericEJB.WalletContextInterceptor;
import com.alodiga.wallet.common.genericEJB.WalletLoggerInterceptor;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.portal.business.commons.data.BusinessData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.Business;

@Interceptors({WalletLoggerInterceptor.class, WalletContextInterceptor.class})
@Stateless(name = EjbConstants.BUSINESS_EJB, mappedName = EjbConstants.BUSINESS_EJB)
@TransactionManagement(TransactionManagementType.BEAN)
public class BusinessEJBImp  implements BusinessEJB {

    private static final Logger logger = Logger.getLogger(BusinessEJBImp.class);
    
    private BusinessData businessData = new BusinessData();
	
	public Business getBusinessById(long id) throws NullParameterException, GeneralException {
		return businessData.getBusinessById(id);
	}

	public Business getBusinessByCode(String code) throws NullParameterException, GeneralException {
		return businessData.getBusinessByCode(code);
	}

	public Business getBusinessByIdentification(String identification) throws NullParameterException, GeneralException {
		return businessData.getBusinessByIdentification(identification);
	}

	public Business getBusinessByEmail(String email) throws NullParameterException, GeneralException {
		return businessData.getBusinessByEmail(email);
	}

	public Business getBusinessByPhone(String phone) throws NullParameterException, GeneralException {
		return businessData.getBusinessByPhone(phone);
	}

	public Business getBusinessByLogin(String login) throws NullParameterException, GeneralException {
		return businessData.getBusinessByLogin(login);
	}

	public List<Business> getAll() throws EmptyListException, GeneralException {
		return businessData.getBusinessList();
	 }
}
