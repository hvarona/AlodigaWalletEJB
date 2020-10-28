package com.alodiga.wallet.ejb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.ejb.UtilsEJB;
import com.alodiga.wallet.common.ejb.UtilsEJBLocal;
import com.alodiga.wallet.common.enumeraciones.PersonClassificationE;
import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.genericEJB.AbstractWalletEJB;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
import com.alodiga.wallet.common.genericEJB.WalletContextInterceptor;
import com.alodiga.wallet.common.genericEJB.WalletLoggerInterceptor;
import com.alodiga.wallet.common.model.AccountBank;
import com.alodiga.wallet.common.model.AccountTypeBank;
import com.alodiga.wallet.common.model.Address;
import com.alodiga.wallet.common.model.AddressType;
import com.alodiga.wallet.common.model.Bank;
import com.alodiga.wallet.common.model.AffiliationRequest;
import com.alodiga.wallet.common.model.BusinessCategory;
import com.alodiga.wallet.common.model.City;
import com.alodiga.wallet.common.model.CivilStatus;
import com.alodiga.wallet.common.model.CollectionType;
import com.alodiga.wallet.common.model.CollectionsRequest;
import com.alodiga.wallet.common.model.Country;
import com.alodiga.wallet.common.model.Currency;
import com.alodiga.wallet.common.model.DocumentType;
import com.alodiga.wallet.common.model.DocumentsPersonType;
import com.alodiga.wallet.common.model.EdificationType;
import com.alodiga.wallet.common.model.LegalPerson;
import com.alodiga.wallet.common.model.NaturalPerson;
import com.alodiga.wallet.common.model.OriginApplication;
import com.alodiga.wallet.common.model.Person;
import com.alodiga.wallet.common.model.PersonClassification;
import com.alodiga.wallet.common.model.PersonHasAddress;
import com.alodiga.wallet.common.model.PersonType;
import com.alodiga.wallet.common.model.PhonePerson;
import com.alodiga.wallet.common.model.PhoneType;
import com.alodiga.wallet.common.model.Profession;
import com.alodiga.wallet.common.model.RequestHasCollectionRequest;
import com.alodiga.wallet.common.model.Sequences;
import com.alodiga.wallet.common.model.State;
import com.alodiga.wallet.common.model.StatusAccountBank;
import com.alodiga.wallet.common.model.StatusApplicant;
import com.alodiga.wallet.common.model.StatusRequest;
import com.alodiga.wallet.common.model.StreetType;
import com.alodiga.wallet.common.utils.Constants;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.alodiga.wallet.common.utils.QueryConstants;

@Interceptors({WalletLoggerInterceptor.class, WalletContextInterceptor.class})
@Stateless(name = EjbConstants.BUSINESS_PORTAL_EJB, mappedName = EjbConstants.BUSINESS_PORTAL_EJB)
@TransactionManagement(TransactionManagementType.BEAN)
public class BusinessPortalEJBImp extends AbstractWalletEJB implements BusinessPortalEJB {

    private static final Logger logger = Logger.getLogger(BusinessPortalEJBImp.class);
    @EJB
    private UtilsEJBLocal utilsEJB;

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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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
                prefixNumberSequence = "APP-";
                break;
            case Constants.ORIGIN_APPLICATION_ADMIN_WALLET_ID:
                prefixNumberSequence = "ADM-";
                break;
            case Constants.ORIGIN_APPLICATION_PORTAL_WEB_ID:
                prefixNumberSequence = "PBW";
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

    @Override
    public AffiliationRequest saveAffiliationRequest(Person person, NaturalPerson naturalPerson, LegalPerson legalPerson, PhonePerson phonePerson, Address address) throws NullParameterException, GeneralException {
        AffiliationRequest affiliatinRequest = new AffiliationRequest();
        try {
            if (person.getPersonTypeId().getIndNaturalPerson()) {
                //Se obtiene la Clasificacion del Solicitante Natural
                String personClassificationCode = PersonClassificationE.NABUAP.getPersonClassificationCode();
                PersonClassification personClassification = (PersonClassification) entityManager.createNamedQuery(QueryConstants.PERSON_CLASSIFICATION_BY_CODE, PersonClassification.class).setParameter(Constants.PARAM_CODE, personClassificationCode).getSingleResult();

                //Se guarda el objeto person en la BD
                person.setCreateDate(new Timestamp(new Date().getTime()));
                if (person.getEmail() != null) {
                    person.setEmail(person.getEmail());
                } else {
                    person.setEmail(null);
                }
                person.setPersonTypeId(person.getPersonTypeId());
                person.setPersonClassificationId(personClassification);
                if (person.getWebSite() != null) {
                    person.setWebSite(person.getWebSite());
                } else {
                    person.setWebSite(null);
                }
                person.setCountryId(person.getCountryId());
                person = (Person) saveEntity(person);

                //Se guarda el objeto NaturalPerson en la BD
                naturalPerson.setPersonId(person);
                naturalPerson.setDocumentsPersonTypeId(naturalPerson.getDocumentsPersonTypeId());
                naturalPerson.setIdentificationNumber(naturalPerson.getIdentificationNumber());
                if (naturalPerson.getIdentificactionNumberOld() != null) {
                    naturalPerson.setIdentificactionNumberOld(naturalPerson.getIdentificactionNumberOld());
                } else {
                    naturalPerson.setIdentificactionNumberOld(null);
                }
                naturalPerson.setDueDateDocumentIdentification(naturalPerson.getDueDateDocumentIdentification());
                naturalPerson.setFirstName(naturalPerson.getFirstName());
                naturalPerson.setLastName(naturalPerson.getLastName());
                if (naturalPerson.getMarriedLastName() != null) {
                    naturalPerson.setMarriedLastName(naturalPerson.getMarriedLastName());
                } else {
                    naturalPerson.setMarriedLastName(null);
                }
                naturalPerson.setGender(naturalPerson.getGender());
                naturalPerson.setPlaceBirth(naturalPerson.getPlaceBirth());
                naturalPerson.setDateBirth(naturalPerson.getDateBirth());
                naturalPerson.setCivilStatusId(naturalPerson.getCivilStatusId());
                if (naturalPerson.getProfessionId() != null) {
                    naturalPerson.setProfessionId(naturalPerson.getProfessionId());
                } else {
                    naturalPerson.setProfessionId(null);
                }
                naturalPerson.setCreateDate(new Timestamp(new Date().getTime()));
                saveEntity(naturalPerson);
            } else {
                String personClassificationCode = PersonClassificationE.LEBUAP.getPersonClassificationCode();
                //Guardo person
                person.setCreateDate(new Timestamp(new Date().getTime()));
                if (person.getEmail() != null) {
                    person.setEmail(person.getEmail());
                } else {
                    person.setEmail(null);
                }
                person.setPersonTypeId(person.getPersonTypeId());
                //person.setPersonClassificationId(person.getPersonClassificationId());
                PersonClassification personClassification = (PersonClassification) entityManager.createNamedQuery(QueryConstants.PERSON_CLASSIFICATION_BY_CODE, PersonClassification.class).setParameter("code", personClassificationCode).getSingleResult();
                person.setPersonClassificationId(personClassification);
                if (person.getWebSite() != null) {
                    person.setWebSite(person.getWebSite());
                } else {
                    person.setWebSite(null);
                }
                person.setCountryId(person.getCountryId());
                person = (Person) saveEntity(person);
                //Guardo Legal Person
                legalPerson.setCreateDate(new Timestamp(new Date().getTime()));
                legalPerson.setPersonId(person);
                legalPerson.setDocumentsPersonTypeId(legalPerson.getDocumentsPersonTypeId());
                legalPerson.setIdentificationNumber(legalPerson.getIdentificationNumber());
                if (legalPerson.getTradeName() != null) {
                    legalPerson.setTradeName(legalPerson.getTradeName());
                } else {
                    legalPerson.setTradeName(null);
                }
                legalPerson.setBusinessName(legalPerson.getBusinessName());
                legalPerson.setBusinessCategoryId(legalPerson.getBusinessCategoryId());
                legalPerson.setRegisterNumber(legalPerson.getRegisterNumber());
                legalPerson.setDateInscriptionRegister(legalPerson.getDateInscriptionRegister());
                legalPerson.setPayedCapital(legalPerson.getPayedCapital());
                saveEntity(legalPerson);
            }
            //Se guarda el objeto PhonePerson en la BD
            phonePerson.setCountryId(phonePerson.getCountryId());
            phonePerson.setCountryCode(phonePerson.getCountryCode());
            phonePerson.setAreaCode(phonePerson.getAreaCode());
            phonePerson.setNumberPhone(phonePerson.getNumberPhone());
            phonePerson.setPersonId(person);
            phonePerson.setPhoneTypeId(phonePerson.getPhoneTypeId());
            if (phonePerson.getExtensionPhoneNumber() != null) {
                phonePerson.setExtensionPhoneNumber(phonePerson.getExtensionPhoneNumber());
            } else {
                phonePerson.setExtensionPhoneNumber(null);
            }
            phonePerson.setIndMainPhone(phonePerson.getIndMainPhone());
            phonePerson.setCreateDate(new Timestamp(new Date().getTime()));
            saveEntity(phonePerson);
            //Guardo Address
            address.setCountryId(address.getCountryId());
            address.setCityId(address.getCityId());
            if (address.getCountyId() != null) {
                address.setCountyId(address.getCountyId());
            } else {
                address.setCountyId(null);
            }
            if (address.getZipCode() != null) {
                address.setZipCode(address.getZipCode());
            } else {
                address.setZipCode(null);
            }
            if (address.getStreetTypeId() != null) {
                address.setStreetTypeId(address.getStreetTypeId());
            } else {
                address.setStreetTypeId(null);
            }
            if (address.getNameStreet() != null) {
                address.setNameStreet(address.getNameStreet());
            } else {
                address.setNameStreet(null);
            }
            address.setEdificationTypeId(address.getEdificationTypeId());
            if (address.getNameEdification() != null) {
                address.setNameEdification(address.getNameEdification());
            } else {
                address.setNameEdification(null);
            }
            if (address.getTower() != null) {
                address.setTower(address.getTower());
            } else {
                address.setTower(null);
            }
            if (address.getFloor() != null) {
                address.setFloor(address.getFloor());
            } else {
                address.setFloor(null);
            }
            if (address.getUrbanization() != null) {
                address.setUrbanization(address.getUrbanization());
            } else {
                address.setUrbanization(null);
            }
            address.setAddressLine1("calle:" + address.getNameStreet() + "," + "Urbanizacion: " + address.getUrbanization() + "," + "Edificio:" + address.getNameEdification() + "," + "Piso:" + address.getFloor() + "");
            address.setAddressLine2("Pais:" + address.getCountryId().getName() + "," + "Ciudad:" + address.getCityId().getName() + "," + "Codigo Postal:" + address.getZipCode() + "");
            address.setAddressTypeId(address.getAddressTypeId());
            address.setIndMainAddress(address.getIndMainAddress());
            address = (Address) saveEntity(address);
            //Guardo Person_has_addres
            PersonHasAddress personHasAddress = new PersonHasAddress();
            personHasAddress.setAddressId(address);
            personHasAddress.setPersonId(person);
            personHasAddress.setCreateDate(new Timestamp(new Date().getTime()));
            saveEntity(personHasAddress);
            
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(Constants.PARAM_CODE, Constants.ORIGIN_APPLICATION_PORTAL_NEGOCIOS_CODE);
            EJBRequest request = new EJBRequest();
            request.setParams(params);
            OriginApplication originApplication = utilsEJB.loadOriginApplicationByCode(request);
            params = new HashMap<String, Object>();
            if (person.getPersonTypeId().getIndNaturalPerson()) {
            	params.put(EjbConstants.PARAM_DOCUMENT_TYPE_ID, naturalPerson.getDocumentsPersonTypeId().getId());
            }else {
            	params.put(EjbConstants.PARAM_DOCUMENT_TYPE_ID, legalPerson.getDocumentsPersonTypeId().getId());	
            }
            request = new EJBRequest();
            request.setParams(params);

            List<Sequences> sequences = getSequencesByDocumentType(request);

            String numberSequence = generateNumberSequence(sequences, originApplication.getId());
            affiliatinRequest.setBusinessPersonId(person);
            affiliatinRequest.setCreateDate(new Timestamp(new Date().getTime()));
            affiliatinRequest.setDateRequest(new Date());
            params = new HashMap<String, Object>();
            params.put(Constants.PARAM_CODE, Constants.STATUS_BUSINESS_AFFILIATION_REQUEST_PENDING);
            request = new EJBRequest();
            request.setParams(params);
            affiliatinRequest.setNumberRequest(numberSequence);
            StatusRequest status = utilsEJB.loadStatusBusinessAffiliationRequestByCode(request);
            affiliatinRequest.setStatusRequestId(status);
            affiliatinRequest = (AffiliationRequest) saveEntity(affiliatinRequest);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);

        }
        return affiliatinRequest;
    }

    @Override
    public RequestHasCollectionRequest saveRequestHasCollectionsRequest(RequestHasCollectionRequest requestHasCollectionsRequest) throws NullParameterException, GeneralException {
        if (requestHasCollectionsRequest == null) {
            throw new NullParameterException("requestHasCollectionsRequest", null);
        }
        return (RequestHasCollectionRequest) saveEntity(requestHasCollectionsRequest);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CivilStatus> getCivilStatus(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        return (List<CivilStatus>) listEntities(CivilStatus.class, request, logger, getMethodName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Profession> getProfession(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        return (List<Profession>) listEntities(Profession.class, request, logger, getMethodName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PhoneType> getPhoneType(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        return (List<PhoneType>) listEntities(PhoneType.class, request, logger, getMethodName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AddressType> getAddressType(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        return (List<AddressType>) listEntities(AddressType.class, request, logger, getMethodName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<EdificationType> getEdificationType(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        return (List<EdificationType>) listEntities(EdificationType.class, request, logger, getMethodName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<StreetType> getStreetType(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        return (List<StreetType>) listEntities(StreetType.class, request, logger, getMethodName());
    }

    @Override
    public DocumentType loadDocumentType(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        DocumentType documentType = (DocumentType) loadEntity(DocumentType.class, request, logger, getMethodName());
        return documentType;
    }

    @Override
    public OriginApplication loadOriginApplication(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        OriginApplication originApplication = (OriginApplication) loadEntity(OriginApplication.class, request, logger, getMethodName());
    return originApplication;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<StatusApplicant> getStatusApplicant(EJBRequest request)throws EmptyListException, GeneralException, NullParameterException{
        return (List<StatusApplicant>) listEntities(StatusApplicant.class, request, logger, getMethodName());
    }
    
    @Override
    public StatusApplicant loadStatusApplicant(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
    	StatusApplicant statusApplicant = (StatusApplicant) loadEntity(StatusApplicant.class, request, logger, getMethodName());
    	return statusApplicant;
    }
    
    @Override
    public PhoneType loadPhoneType(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
    	PhoneType phoneType = (PhoneType) loadEntity(PhoneType.class, request, logger, getMethodName());
    	return phoneType;
    }
    
    @Override
    public AffiliationRequest loadAffiliationRequest(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
    	AffiliationRequest affiliationRequest = (AffiliationRequest) loadEntity(AffiliationRequest.class, request, logger, getMethodName());
    	return affiliationRequest;
    }
    
    @Override
    public Bank loadBank(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
    	Bank bank = (Bank) loadEntity(Bank.class, request, logger, getMethodName());
    	return bank;
    }
    
    @Override
    public AccountTypeBank loadAccountTypeBank(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
    	AccountTypeBank accountTypeBank = (AccountTypeBank) loadEntity(AccountTypeBank.class, request, logger, getMethodName());
    	return accountTypeBank;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Bank> getBanks(EJBRequest request)throws EmptyListException, GeneralException, NullParameterException{
        return (List<Bank>) listEntities(Bank.class, request, logger, getMethodName());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<AccountTypeBank> getAccountTypeBanks(EJBRequest request)throws EmptyListException, GeneralException, NullParameterException{
        return (List<AccountTypeBank>) listEntities(AccountTypeBank.class, request, logger, getMethodName());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<AccountBank> getAccountBanks(EJBRequest request)throws EmptyListException, GeneralException, NullParameterException{
        return (List<AccountBank>) listEntities(AccountBank.class, request, logger, getMethodName());
    }
    
    @Override
    public AccountBank loadAccountBank(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException{
    	AccountBank accountBank = (AccountBank) loadEntity(AccountBank.class, request, logger, getMethodName());
    	return accountBank;
    }
    
    @Override
    public AccountBank saveAccountBank(AccountBank accountBank) throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (accountBank == null) {
            throw new NullParameterException("accountBank", null);
        }
        return (AccountBank) saveEntity(accountBank);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Currency> getCurrencies(EJBRequest request)throws EmptyListException, GeneralException, NullParameterException{
        return (List<Currency>) listEntities(Currency.class, request, logger, getMethodName());
    }
    
    @Override
    public Currency loadCurrency(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException{
    	Currency currency = (Currency) loadEntity(Currency.class, request, logger, getMethodName());
    	return currency;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<StatusAccountBank> getStatusAccountBanks(EJBRequest request)throws EmptyListException, GeneralException, NullParameterException{
        return (List<StatusAccountBank>) listEntities(StatusAccountBank.class, request, logger, getMethodName());
    }
    
    @Override
    public StatusAccountBank loadStatusAccountBank(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException{
    	StatusAccountBank currency = (StatusAccountBank) loadEntity(StatusAccountBank.class, request, logger, getMethodName());
    	return currency;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<BusinessCategory> getBusinessCategories(EJBRequest request)throws EmptyListException, GeneralException, NullParameterException{
        return (List<BusinessCategory>) listEntities(BusinessCategory.class, request, logger, getMethodName());
    }
    
    @Override
    public BusinessCategory loadBusinessCategory(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException{
    	BusinessCategory category = (BusinessCategory) loadEntity(BusinessCategory.class, request, logger, getMethodName());
    	return category;
    }
}
