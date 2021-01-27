package com.infamous.framework.http;

import java.util.Objects;

public class GithubUserVo {

    String m_login;
    String m_id;

    public GithubUserVo() {

    }

    public GithubUserVo(String login, String id) {
        m_login = login;
        m_id = id;
    }

    public String getLogin() {
        return m_login;
    }

    public GithubUserVo setLogin(String login) {
        this.m_login = login;
        return this;
    }

    public String getId() {
        return m_id;
    }

    public GithubUserVo setId(String id) {
        this.m_id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GithubUserVo that = (GithubUserVo) o;
        return Objects.equals(m_login, that.m_login) &&
            Objects.equals(m_id, that.m_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_login, m_id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GithubUserVo{");
        sb.append("m_login='").append(m_login).append('\'');
        sb.append(", m_id='").append(m_id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
