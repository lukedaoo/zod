package com.infamous.zod.ftp.um;

import org.apache.ftpserver.ftplet.UserManager;

public interface FTPUserManager extends UserManager {
    String hashPassword(String rawPassword);
}
