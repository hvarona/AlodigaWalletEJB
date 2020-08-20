package com.alodiga.wallet.ejb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.ejb.UtilsEJB;
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
import com.alodiga.wallet.common.exception.NoResultException;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
import com.alodiga.wallet.common.utils.Constants;
import com.alodiga.wallet.common.utils.QueryConstants;
import java.util.Calendar;
import java.util.Map;
import javax.persistence.Query;

@Interceptors({WalletLoggerInterceptor.class, WalletContextInterceptor.class})
@Stateless(name = EjbConstants.BUSINESS_PORTAL_EJB, mappedName = EjbConstants.BUSINESS_PORTAL_EJB)
@TransactionManagement(TransactionManagementType.BEAN)
public class BusinessPortalEJBImp extends AbstractWalletEJB implements BusinessPortalEJB {

    private static final Logger logger = Logger.getLogger(BusinessPortalEJBImp.class);

    @Override
    public List<PersonType> getPersonTypesBycountryId(Long countryId) throws EmptyListException, GeneralException, NullParameterException {
        if (countryId == null) {
            throw new NullParameterException("countryId", null);
        }
        List<PersonType> personTypes = new ArrayList<PersonType>();
        try {
            personTypes = (List<PersonType>) entityManager.createNamedQuery("PersonType.findBycountryId", PersonType.class).setParameter("countryId", countryId).getResultList();
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (personTypes.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return personTypes;
    }

    @Override
    public List<DocumentsPersonType> getDocumentPersonTypesBypersonTypeId(Long personTypeId) throws EmptyListException, GeneralException, NullParameterException {
        if (personTypeId == null) {
            throw new NullParameterException("personTypeId", null);
        }
        List<DocumentsPersonType> documentsPersonTypes = new ArrayList<DocumentsPersonType>();
        try {
            documentsPersonTypes = (List<DocumentsPersonType>) entityManager.createNamedQuery("DocumentsPersonType.findBypersonTypeId", DocumentsPersonType.class).setParameter("personTypeId", personTypeId).getResultList();
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (documentsPersonTypes.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return documentsPersonTypes;
    }

    @Override
    public List<CollectionType> getCollectionTypesBycountryId(Long countryId) throws EmptyListException, GeneralException, NullParameterException {
        if (countryId == null) {
            throw new NullParameterException("countryId", null);
        }
        List<CollectionType> collectionTypes = new ArrayList<CollectionType>();
        try {
            collectionTypes = (List<CollectionType>) entityManager.createNamedQuery("CollectionType.findBycountryId", CollectionType.class).setParameter("countryId", countryId).getResultList();
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (collectionTypes.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return collectionTypes;
    }

    @Override
    public List<CollectionsRequest> getCollectionRequestsBycollectionTypeId(Long collectionTypeId) throws EmptyListException, GeneralException, NullParameterException {
        if (collectionTypeId == null) {
            throw new NullParameterException("collectionTypeId", null);
        }
        List<CollectionsRequest> collectionsRequests = new ArrayList<CollectionsRequest>();
        try {
            collectionsRequests = (List<CollectionsRequest>) entityManager.createNamedQuery("CollectionsRequest.findBycollectionTypeId", CollectionsRequest.class).setParameter("collectionTypeId", collectionTypeId).getResultList();
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (collectionsRequests.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return collectionsRequests;
    }

    @Override
    public List<Country> getCountries() throws EmptyListException, GeneralException, NullParameterException {
        List<Country> countries = null;
        Query query = null;
        try {
            query = createQuery("SELECT c FROM Country c ORDER BY c.name");
            countries = query.setHint("toplink.refresh", "true").getResultList();
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (countries.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return countries;
    }

    @Override
    public List<State> getStatesByCountryId(Long countryId) throws EmptyListException, GeneralException, NullParameterException {
        if (countryId == null) {
            throw new NullParameterException("countryId", null);
        }
        List<State> states = new ArrayList<State>();
        try {
            states = (List<State>) entityManager.createNamedQuery("State.findBycountryId", State.class).setParameter("countryId", countryId).getResultList();
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (states.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return states;
    }

    @Override
    public List<City> getCitiesByStateId(Long stateId) throws EmptyListException, GeneralException, NullParameterException {
        if (stateId == null) {
            throw new NullParameterException("stateId", null);
        }
        List<City> citys = new ArrayList<City>();
        try {
            citys = (List<City>) entityManager.createNamedQuery("City.findBystateId", City.class).setParameter("stateId", stateId).getResultList();
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (citys.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return citys;
    }

    @Override
    public List<Sequences> getSequencesByDocumentType(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        List<Sequences> sequence = null;
        Map<String, Object> params = request.getParams();
        if (!params.containsKey(EjbConstants.PARAM_DOCUMENT_TYPE_ID)) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), EjbConstants.PARAM_DOCUMENT_TYPE_ID), null);
        }
        sequence = (List<Sequences>) getNamedQueryResult(UtilsEJB.class, QueryConstants.SEQUENCES_BY_DOCUMENT_TYPE, request, getMethodName(), logger, "sequence");
        return sequence;
    }

    @Override
    public String generateNumberSequence(List<Sequences> sequence, int originApplication) throws GeneralException, RegisterNotFoundException, NullParameterException {
        int numberSequence = 0;
        String prefixNumberSequence = "";
        String acronym = "";
        for (Sequences s : sequence) {
            if (s.getOriginApplicationId().getId() == originApplication) {
                if (s.getCurrentValue() > 1) {
                    numberSequence = s.getCurrentValue();
                } else {
                    numberSequence = s.getInitialValue();
                }
                acronym = s.getDocumentTypeId().getAcronym();
                s.setCurrentValue(s.getCurrentValue() + 1);
                saveSequences(s);
            }
        }
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        switch (originApplication) {
            case Constants.ORIGIN_APPLICATION_APP_ALODIGA_WALLET_ID:
                prefixNumberSequence = "CMS-";
                break;
            case Constants.ORIGIN_APPLICATION_ADMIN_WALLET_ID:
                prefixNumberSequence = "APP-";
                break;
            default:
                break;
        }
        prefixNumberSequence = prefixNumberSequence.concat(acronym);
        String suffixNumberSequence = "-";
        suffixNumberSequence = suffixNumberSequence.concat(String.valueOf(year));
        String numberSequenceDoc = prefixNumberSequence;
        numberSequenceDoc = numberSequenceDoc.concat("-");
        numberSequenceDoc = numberSequenceDoc.concat(String.valueOf(numberSequence));
        numberSequenceDoc = numberSequenceDoc.concat(suffixNumberSequence);
        return numberSequenceDoc;
    }

    @Override
    public Sequences saveSequences(Sequences sequence) throws RegisterNotFoundException, NullParameterException, GeneralException {
       if (sequence == null) {
           throw new NullParameterException("sequence", null);
       }
       return (Sequences) saveEntity(sequence);
   }

}
