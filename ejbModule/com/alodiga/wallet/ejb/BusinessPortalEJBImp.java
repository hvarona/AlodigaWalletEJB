package com.alodiga.wallet.ejb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.genericEJB.AbstractWalletEJB;
import com.alodiga.wallet.common.genericEJB.WalletContextInterceptor;
import com.alodiga.wallet.common.genericEJB.WalletLoggerInterceptor;
import com.alodiga.wallet.common.model.AccountBank;
import com.alodiga.wallet.common.model.City;
import com.alodiga.wallet.common.model.CollectionType;
import com.alodiga.wallet.common.model.CollectionsRequest;
import com.alodiga.wallet.common.model.Country;
import com.alodiga.wallet.common.model.DocumentsPersonType;
import com.alodiga.wallet.common.model.PersonType;
import com.alodiga.wallet.common.model.Sequences;
import com.alodiga.wallet.common.model.State;
import com.alodiga.wallet.common.model.TransactionApproveRequest;
import com.alodiga.wallet.common.utils.EjbConstants;

@Interceptors({WalletLoggerInterceptor.class, WalletContextInterceptor.class})
@Stateless(name = EjbConstants.BUSINESS_PORTAL_EJB, mappedName = EjbConstants.BUSINESS_PORTAL_EJB)
@TransactionManagement(TransactionManagementType.BEAN)
public class BusinessPortalEJBImp extends AbstractWalletEJB implements BusinessPortalEJB {

    private static final Logger logger = Logger.getLogger(BusinessPortalEJBImp.class);

	@Override
	public List<PersonType> getPersonTypesBycountryId(Long countryId)throws EmptyListException, GeneralException, NullParameterException {
		if (countryId == null) {
	          throw new NullParameterException("countryId", null);
	    }
		List<PersonType> personTypes = new ArrayList<PersonType>();
		try {
			personTypes = (List<PersonType>) entityManager.createNamedQuery("PersonType.findBycountryId", PersonType.class).setParameter("countryId", countryId).getResultList();
		} catch (Exception e) {
			throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(),getMethodName(), e.getMessage()), null);
		}
		if(personTypes.isEmpty())
			 throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
		return personTypes;
	}

	@Override
	public List<DocumentsPersonType> getDocumentPersonTypesBypersonTypeId(Long personTypeId)throws EmptyListException, GeneralException, NullParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CollectionType> getCollectionTypesBycountryId(Long countryId)throws EmptyListException, GeneralException, NullParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CollectionsRequest> getCollectionRequestsBycollectionTypeId(Long collectionTypeId)throws EmptyListException, GeneralException, NullParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Country> getCountries() throws EmptyListException, GeneralException, NullParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<State> getStatesByCountryId(Long countryId)	throws EmptyListException, GeneralException, NullParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<City> getCitiesByStateId(Long stateId)throws EmptyListException, GeneralException, NullParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sequences getSequencesByDocumentTypeByOriginApplication(Long documentTypeId, Long originApplicationId)throws RegisterNotFoundException, GeneralException, NullParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionApproveRequest saveTransactionApproveRequest(Long unifiedRegistryUserId, Long productId,Long transactionId, Long bankOperationId, Long documentTypeId, Long originApplicationId)			throws NullParameterException, GeneralException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccountBank saveAccountBank(Long unifiedRegistryId, String accountNumber, Long bankId,Integer accountTypeBankId) throws NullParameterException, GeneralException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccountBank> getAccountBankByUser(Long unifiedRegistryId)throws EmptyListException, GeneralException, NullParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccountBank getAccountBankByUserByBank(Long unifiedRegistryId, Long bankId)throws RegisterNotFoundException, NullParameterException, GeneralException {
		// TODO Auto-generated method stub
		return null;
	}

    
}
