package com.blue.girl.server.service;

import com.blue.girl.server.dao.FileRecordDao;
import com.blue.girl.server.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseService {
    @Autowired
    protected UserDao userDao;

    @Autowired
    protected FileRecordDao fileRecordDao;
}
