package com.alodiga.wallet.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import com.alodiga.wallet.common.ejb.UtilsEJB;
import com.alodiga.wallet.common.ejb.UtilsEJBLocal;
import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.genericEJB.AbstractWalletEJB;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
import com.alodiga.wallet.common.genericEJB.WalletContextInterceptor;
import com.alodiga.wallet.common.genericEJB.WalletLoggerInterceptor;
import com.alodiga.wallet.common.model.Bank;
import com.alodiga.wallet.common.model.City;
import com.alodiga.wallet.common.model.Close;
import com.alodiga.wallet.common.model.Country;
import com.alodiga.wallet.common.model.County;
import com.alodiga.wallet.common.model.Currency;
import com.alodiga.wallet.common.model.Enterprise;
import com.alodiga.wallet.common.model.ExchangeRate;
import com.alodiga.wallet.common.model.Language;
import com.alodiga.wallet.common.model.Period;
import com.alodiga.wallet.common.model.Sms;
import com.alodiga.wallet.common.model.State;
import com.alodiga.wallet.common.model.Transaction;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.alodiga.wallet.common.utils.GeneralUtils;
import com.alodiga.wallet.common.utils.QueryConstants;
import java.text.SimpleDateFormat;
import java.util.Date;

@Interceptors({WalletLoggerInterceptor.class, WalletContextInterceptor.class})
@Stateless(name = EjbConstants.UTILS_EJB, mappedName = EjbConstants.UTILS_EJB)
@TransactionManagement(TransactionManagementType.BEAN)
public class UtilsEJBImp extends AbstractWalletEJB implements UtilsEJB, UtilsEJBLocal {

    private static final Logger logger = Logger.getLogger(UtilsEJBImp.class);

    public List<City> getCitiesByCounty(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        List<City> cities = null;
        Map<String, Object> params = request.getParams();
        if (!params.containsKey(QueryConstants.PARAM_COUNTY_ID)) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), QueryConstants.PARAM_COUNTY_ID), null);
        }
        cities = (List<City>) getNamedQueryResult(UtilsEJB.class, QueryConstants.CITIES_BY_COUNTY, request, getMethodName(), logger, "cities");
        return cities;
    }

    public List<City> getCitiesByState(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        List<City> cities = null;
        Map<String, Object> params = request.getParams();
        if (!params.containsKey(QueryConstants.PARAM_STATE_ID)) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "id"), null);
        }

        cities = (List<City>) getNamedQueryResult(UtilsEJB.class, QueryConstants.CITIES_BY_STATE, request, getMethodName(), logger, "cities");

        return cities;
    }

    public City loadCity(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        City city = (City) loadEntity(City.class, request, logger, getMethodName());
        return city;
    }

    //County
    public List<County> getCountiesByState(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        List<County> counties = null;
        Map<String, Object> params = request.getParams();

        if (!params.containsKey(QueryConstants.PARAM_STATE_ID)) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), QueryConstants.PARAM_STATE_ID), null);
        }
        counties = (List<County>) getNamedQueryResult(UtilsEJB.class, QueryConstants.COUNTIES_BY_STATE, request, getMethodName(), logger, "counties");
        return counties;
    }

    public County loadCounty(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        County county = (County) loadEntity(County.class, request, logger, getMethodName());
        return county;
    }

    //Country
    public List<Country> getCountries(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        List<Country> countries = (List<Country>) listEntities(Country.class, request, logger, getMethodName());
        return countries;
    }

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

    public Country loadCountry(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        Country country = (Country) loadEntity(Country.class, request, logger, getMethodName());
        return country;
    }

    public Country loadCountryByName(String name) throws RegisterNotFoundException, NullParameterException, GeneralException {
        List<Country> list = new ArrayList<Country>();
        Country country = new Country();

        try {
            if (name == null) {
                throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "name"), null);
            }
            StringBuilder sqlBuilder = new StringBuilder("SELECT DISTINCT ct.country FROM CountryTranslation ct ");
            sqlBuilder.append("WHERE ct.alias LIKE '").append(name).append("'")//Problema con el caso FRANCE y GUYANA FRANCESA
                    .append(" OR ct.country.alternativeName1 LIKE '").append(name).append("'").append(" OR ct.country.alternativeName2 LIKE '%").append(name).append("'").append(" OR ct.country.alternativeName3 LIKE '%").append(name).append("'");
            //country = (Country) createQuery(sqlBuilder.toString()).setHint("toplink.refresh", "true").getSingleResult();
            list = createQuery(sqlBuilder.toString()).setHint("toplink.refresh", "true").getResultList();
            if (list.isEmpty()) {
                System.out.println("name: " + name);
                throw new RegisterNotFoundException(logger, sysError.format(EjbConstants.ERR_REGISTER_NOT_FOUND_EXCEPTION, Country.class.getSimpleName(), "loadCountryByName", Country.class.getSimpleName(), null), null);
            } else {
                country = list.get(0);
            }
        } catch (RegisterNotFoundException ex) {
            System.out.println("name: " + name);
            throw new RegisterNotFoundException(logger, sysError.format(EjbConstants.ERR_REGISTER_NOT_FOUND_EXCEPTION, Country.class.getSimpleName(), "loadCountryByName", Country.class.getSimpleName(), null), ex);
        } catch (Exception ex) {
            System.out.println("name: " + name);
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), ex.getMessage()), ex);
        }

        return country;
    }

    public Country loadCountryByShortName(String referenceCode) throws RegisterNotFoundException, NullParameterException, GeneralException {
        Country country = new Country();
        try {
            if (referenceCode == null) {
                throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "referenceCode"), null);
            }

            Query query = createQuery("SELECT c FROM Country c WHERE c.shortName = ?1");
            query.setParameter("1", referenceCode);
            country = (Country) query.setHint("toplink.refresh", "true").getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("shortName: " + referenceCode);
            throw new RegisterNotFoundException(logger, sysError.format(EjbConstants.ERR_REGISTER_NOT_FOUND_EXCEPTION, Country.class.getSimpleName(), "loadCountryByShortName", Country.class.getSimpleName(), null), ex);
        } catch (Exception ex) {
            System.out.println("shortName: " + referenceCode);
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), ex.getMessage()), ex);
        }
        return country;
    }

    public Country searchCountry(String name) throws RegisterNotFoundException, NullParameterException, GeneralException {
        Country country = new Country();

        try {
            if (name == null) {
                throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "name"), null);
            }
            StringBuilder sqlBuilder = new StringBuilder("SELECT DISTINCT ct.country FROM CountryTranslation ct ");
            sqlBuilder.append("WHERE ct.country.name LIKE '").append(name).append("'").append(" OR ct.alias LIKE '").append(name).append("'")//Problema con el caso FRANCE y GUYANA FRANCESA
                    .append(" OR ct.country.alternativeName1 LIKE '%").append(name).append("%'").append(" OR ct.country.alternativeName2 LIKE '%").append(name).append("'").append(" OR ct.country.alternativeName3 LIKE '%").append(name).append("'").append(" OR ct.country.shortName LIKE '").append(name).append("'");
            country = (Country) createQuery(sqlBuilder.toString()).setHint("toplink.refresh", "true").getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("name: " + name);
            throw new RegisterNotFoundException(logger, sysError.format(EjbConstants.ERR_REGISTER_NOT_FOUND_EXCEPTION, Country.class.getSimpleName(), "loadCountryByName", Country.class.getSimpleName(), null), ex);
        } catch (Exception ex) {
            System.out.println("name: " + name);
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), ex.getMessage()), ex);
        }

        return country;
    }

    public Country saveCountry(Country country) throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (country == null) {
            throw new NullParameterException("country", null);
        }
        return (Country) saveEntity(country);
    }

    //Enterprise
    public List<Enterprise> getEnterprises(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        List<Enterprise> enterprise = (List<Enterprise>) listEntities(Enterprise.class, request, logger, getMethodName());
        return enterprise;
    }

    public List<Enterprise> getEnterprises() throws EmptyListException, GeneralException, NullParameterException {
        EJBRequest request = new EJBRequest();
        List<Enterprise> enterprises = (List<Enterprise>) listEntities(Enterprise.class, request, logger, getMethodName());

        return enterprises;
    }

    public Enterprise loadEnterprise(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        Enterprise enterprise = (Enterprise) loadEntity(Enterprise.class, request, logger, getMethodName());
        return enterprise;
    }

    public Enterprise loadEnterprisebyId(Long enterpriseId) throws GeneralException {
        List<Enterprise> list = new ArrayList();

        try {
            list = entityManager.createQuery("SELECT c FROM Enterprise c WHERE c.id='" + enterpriseId + "'").getResultList();
        } catch (Exception e) {

            logger.error("Exception in method loadEnterprise: Exception text: ", e);
            throw new GeneralException("Exception in method loadEnterprise: Exception text: " + e.getMessage(), e.getStackTrace());
        }
        if (list.isEmpty()) {
            logger.error("Not Enterprise found in method loadEnterprise");
            //throw new EnterpriseNotFoundException("Not Enterprise found in method loadEnterprise");
        }

        return list.get(0);
    }

    public Enterprise saveEnterprise(EJBRequest request) throws NullParameterException, GeneralException {
        return (Enterprise) saveEntity(request, logger, getMethodName());
    }

    public void deleteEnterpriseHasTinType(Long enterpriseId) throws NullParameterException, GeneralException {
        if (enterpriseId == null) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "enterpriseId"), null);
        }

        try {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            Query query = createQuery("DELETE FROM EnterpriseHasTinType ehhtt WHERE ehhtt.enterprise.id=?1");
            query.setParameter("1", enterpriseId);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
    }

    //Language
    public List<Language> getLanguages() throws EmptyListException, GeneralException, NullParameterException {
        EJBRequest request = new EJBRequest();
        List<Language> languages = (List<Language>) listEntities(Language.class, request, logger, getMethodName());
        return languages;
    }

    public Language loadLanguage(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        Language language = (Language) loadEntity(Language.class, request, logger, getMethodName());
        return language;
    }

    //Period
    public List<Period> getPeriods() throws EmptyListException, GeneralException, NullParameterException {
        EJBRequest request = new EJBRequest();
        List<Period> periods = (List<Period>) listEntities(Period.class, request, logger, getMethodName());

        return periods;
    }

    public Period loadPeriod(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        Period period = (Period) loadEntity(Period.class, request, logger, getMethodName());
        return period;
    }

    public Period loadperiod(Period period) throws RegisterNotFoundException, NullParameterException, GeneralException {
        Period periods = (Period) loadEntity(Period.class, period, logger, getMethodName());
        return periods;
    }

    //State
    public List<State> getStateByCountry(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        List<State> states = null;
        Map<String, Object> params = request.getParams();
        if (!params.containsKey(QueryConstants.PARAM_COUNTRY_ID)) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), QueryConstants.PARAM_COUNTRY_ID), null);
        }
        states = (List<State>) getNamedQueryResult(UtilsEJB.class, QueryConstants.STATES_BY_COUNTRY, request, getMethodName(), logger, "states");
        return states;
    }

    public State loadState(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        State state = (State) loadEntity(State.class, request, logger, getMethodName());
        return state;
    }

    //Currency
    public List<Currency> getCurrency(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        return (List<Currency>) listEntities(Currency.class, request, logger, getMethodName());
    }

    public List<Currency> getCurrencies() throws EmptyListException, GeneralException, NullParameterException {
        EJBRequest request = new EJBRequest();
        List<Currency> currencies = (List<Currency>) listEntities(Currency.class, request, logger, getMethodName());

        return currencies;
    }

    @Override
    public List<Currency> getSearchCurrency(String name) throws EmptyListException, GeneralException, NullParameterException {
        List<Currency> currencyList = null;
        if (name == null) {
            throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "name"), null);
        }
        try {
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM currency c ");
            sqlBuilder.append("WHERE c.name LIKE '%").append(name).append("%'");
            Query query = entityManager.createNativeQuery(sqlBuilder.toString(), Currency.class);
            currencyList = query.setHint("toplink.refresh", "true").getResultList();

        } catch (NoResultException ex) {
            throw new EmptyListException("No distributions found");
        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        return currencyList;
    }

    @Override
    public Currency loadCurrency(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        Currency currency = (Currency) loadEntity(Currency.class, request, logger, getMethodName());
        return currency;
    }

    @Override
    public Currency saveCurrency(Currency currency) throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (currency == null) {
            throw new NullParameterException("currency", null);
        }
        return (Currency) saveEntity(currency);
    }

    //SMS
    public Sms saveSMS(EJBRequest request) throws NullParameterException, GeneralException {
        return (Sms) saveEntity(request, logger, getMethodName());
    }

    //Bank
    @Override
    public List<Bank> getBank(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        return (List<Bank>) listEntities(Bank.class, request, logger, getMethodName());
    }

    @Override
    public Bank loadBank(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        Bank bank = (Bank) loadEntity(Bank.class, request, logger, getMethodName());
        return bank;
    }

    @Override
    public Bank saveBank(Bank bank) throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (bank == null) {
            throw new NullParameterException("bank", null);
        }
        return (Bank) saveEntity(bank);
    }

    //ExchangeRate
    public List<ExchangeRate> getExchangeRate(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        return (List<ExchangeRate>) listEntities(ExchangeRate.class, request, logger, getMethodName());
    }

    @Override
    public ExchangeRate loadExchangeRate(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        ExchangeRate exchangeRate = (ExchangeRate) loadEntity(ExchangeRate.class, request, logger, getMethodName());
        return exchangeRate;
    }

    @Override
    public ExchangeRate saveExchangeRate(ExchangeRate exchangeRate) throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (exchangeRate == null) {
            throw new NullParameterException("exchangeRate", null);
        }
        return (ExchangeRate) saveEntity(exchangeRate);
    }

    //Transaction
    @Override
    public List<Transaction> getTransaction(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        List<Transaction> transactions = (List<Transaction>) listEntities(Transaction.class, request, logger, getMethodName());
        return transactions;
    }
    
    public List<Transaction> getTransactionByDates(Date beginningDate, Date endingDate) throws RegisterNotFoundException, NullParameterException, GeneralException, EmptyListException {
        List<Transaction> transactionsList = new ArrayList<Transaction>();
        
        try {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            
            if (beginningDate == null || endingDate == null) {
                throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "beginningDate & endingDate"), null);
            }
            
            String strDate1 = (simpleDateFormat.format(beginningDate));
            String strDate2 = (simpleDateFormat.format(endingDate));
            
            StringBuilder sqlBuilder = new StringBuilder("select * from transaction t where t.creationDate BETWEEN '");
            sqlBuilder.append(strDate1);
            sqlBuilder.append("' AND '");
            sqlBuilder.append(strDate2);
            sqlBuilder.append("'");
            Query query = entityManager.createNativeQuery(sqlBuilder.toString(), Transaction.class);
            transactionsList = (List<Transaction>) query.setHint("toplink.refresh", "true").getResultList();

        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (transactionsList.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return transactionsList;
    }
 
    public List<Transaction> getTransactionByBeginningDate(Date beginningDate) throws EmptyListException, GeneralException, NullParameterException {
        List<Transaction> transactionsList = new ArrayList<Transaction>();
        try {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            
            if (beginningDate == null) {
                throw new NullParameterException(sysError.format(EjbConstants.ERR_NULL_PARAMETER, this.getClass(), getMethodName(), "beginningDate & endingDate"), null);
            }
            
            String strDate = (simpleDateFormat.format(beginningDate));
            
            StringBuilder sqlBuilder = new StringBuilder("select * from transaction t where t.creationDate like '");
            sqlBuilder.append(strDate);
            sqlBuilder.append("%'");
            Query query = entityManager.createNativeQuery(sqlBuilder.toString(), Transaction.class);
            transactionsList = (List<Transaction>) query.setHint("toplink.refresh", "true").getResultList();

        } catch (Exception e) {
            throw new GeneralException(logger, sysError.format(EjbConstants.ERR_GENERAL_EXCEPTION, this.getClass(), getMethodName(), e.getMessage()), null);
        }
        if (transactionsList.isEmpty()) {
            throw new EmptyListException(logger, sysError.format(EjbConstants.ERR_EMPTY_LIST_EXCEPTION, this.getClass(), getMethodName()), null);
        }
        return transactionsList;
    }
    
    @Override
    public Transaction loadTransaction(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        Transaction transaction = (Transaction) loadEntity(Transaction.class, request, logger, getMethodName());
        return transaction;
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (transaction == null) {
            throw new NullParameterException("transaction", null);
        }
        return (Transaction) saveEntity(transaction);
    }

    //Close
    public List<Close> getClose(EJBRequest request) throws EmptyListException, GeneralException, NullParameterException {
        return (List<Close>) listEntities(Close.class, request, logger, getMethodName());
    }

    public Close loadClose(EJBRequest request) throws RegisterNotFoundException, NullParameterException, GeneralException {
        Close close = (Close) loadEntity(Close.class, request, logger, getMethodName());
        return close;
    }

    public Close saveClose(Close close) throws RegisterNotFoundException, NullParameterException, GeneralException {
        if (close == null) {
            throw new NullParameterException("close", null);
        }
        return (Close) saveEntity(close);
    }
}
