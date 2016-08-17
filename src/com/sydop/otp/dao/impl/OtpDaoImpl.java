package com.sydop.otp.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sydop.otp.dao.OtpDao;
import com.sydop.otp.model.OtpUser;

@Repository(value="otpDao")
public class OtpDaoImpl
implements OtpDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public OtpUser verifyUser(OtpUser vo) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(OtpUser.class).add((Criterion)Restrictions.eq((String)"id", (Object)vo.getId())).add((Criterion)Restrictions.eq((String)"password", (Object)vo.getPassword()));
        return (OtpUser)criteria.uniqueResult();
    }

    public OtpUser verifyUserByToken(String token) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(OtpUser.class).add((Criterion)Restrictions.eq((String)"accessToken", (Object)token));
        return (OtpUser)criteria.uniqueResult();
    }

    public void updateOtpUser(OtpUser vo) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        session.update((Object)vo);
    }

    public OtpUser verifySyncCode(OtpUser vo) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(OtpUser.class)
        		.add((Criterion)Restrictions.eq((String)"syncCode", (Object)vo.getSyncCode()));
        
        criteria.setFlushMode(FlushMode.COMMIT);   
        return (OtpUser)criteria.uniqueResult();
    }

    public OtpUser verifyPin(OtpUser vo) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria((Class)OtpUser.class).add((Criterion)Restrictions.eq((String)"id", (Object)vo.getId())).add((Criterion)Restrictions.eq((String)"pin", (Object)vo.getPin()));
        return (OtpUser)criteria.uniqueResult();
    }
    
    public OtpUser validatePin(OtpUser user) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(OtpUser.class)
        		.add(Restrictions.eq("pin", user.getPin()));
          
        return (OtpUser)criteria.uniqueResult();
    }
}
