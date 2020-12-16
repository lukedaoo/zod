package com.infamous.zod.ftp.model;

import com.infamous.framework.common.SystemPropertyUtils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

@Entity(name = "FTPUser")
@Table(name = "FTPUser")
@IdClass(FTPUserKey.class)
@Builder
@AllArgsConstructor
public class FTPUser implements User {

    @Id
    @Column(nullable = false)
    private String username;
    private String password;
    private String workspace;
    private boolean enabled;

    private int idleTime;
    private boolean isAdmin;

    private int maxDownloadRate = 0;
    private int maxUploadRate = 0;

    private int maxConcurrentLogins;

    @Transient
    private final Map<String, Authority> authorities = new HashMap<>();

    @Transient
    private final Map<String, Authority> adminAuthorities = new HashMap<>();

    public FTPUser() {

    }

    public FTPUser(FTPUserName username, FTPPassword password) {
        this.username = username.getUsername();
        this.password = password.getPassword();
    }

    public void addDefaultAuthorities() {
        if (this.isAdmin()) {
            this.addAdminAuthority("writePermission", new WritePermission());
        }
        this.addAuthority("transferRatePermission",
            new TransferRatePermission(this.getMaxDownloadRate(), this.getMaxUploadRate()));
        this.addAuthority("concurrentLoginPermission",
            new ConcurrentLoginPermission(this.getMaxConcurrentLogins(),
                SystemPropertyUtils.getInstance().getAsInt("ftp.admin.maxConcurrentLogins", 2)));
    }

    public int getMaxDownloadRate() {
        return maxDownloadRate;
    }

    public int getMaxUploadRate() {
        return maxUploadRate;
    }

    public int getMaxConcurrentLogins() {
        return maxConcurrentLogins;
    }

    public void addAdminAuthority(String name, Authority auth) {
        adminAuthorities.put(name, auth);
    }

    public void addAuthority(String name, Authority auth) {
        authorities.put(name, auth);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password; // hashed
    }

    @Override
    public List<? extends Authority> getAuthorities() {
        List<Authority> res = new LinkedList<>();
        if (isAdmin()) {
            res.addAll(adminAuthorities.values());
        }
        res.addAll(authorities.values());
        return res;
    }

    @Override
    public List<? extends Authority> getAuthorities(Class<? extends Authority> aClass) {
        return getAuthorities().stream().filter(a -> a.getClass().equals(aClass)).collect(Collectors.toList());
    }

    @Override
    public AuthorizationRequest authorize(AuthorizationRequest authorizationRequest) {
        return getAuthorities().stream()
            .filter(a -> a.canAuthorize(authorizationRequest))
            .map(a -> a.authorize(authorizationRequest))
            .filter(Objects::nonNull)
            .findFirst().orElse(null);
    }

    @Override
    public int getMaxIdleTime() {
        return idleTime;
    }

    @Override
    public boolean getEnabled() {
        return enabled;
    }

    @Override
    public String getHomeDirectory() {
        return workspace;
    }
}
