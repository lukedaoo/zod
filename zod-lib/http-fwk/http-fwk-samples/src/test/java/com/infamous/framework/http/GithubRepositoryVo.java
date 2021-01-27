package com.infamous.framework.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class GithubRepositoryVo {

    @JsonProperty("id")
    String m_id;
    @JsonProperty("full_name")
    String m_fullName;

    public GithubRepositoryVo() {

    }

    public GithubRepositoryVo(String id, String fullName) {
        this.m_id = id;
        this.m_fullName = fullName;
    }

    public String getId() {
        return m_id;
    }

    public GithubRepositoryVo setId(String id) {
        m_id = id;
        return this;
    }

    public String getFullName() {
        return m_fullName;
    }

    public GithubRepositoryVo setFullName(String fullName) {
        m_fullName = fullName;
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
        GithubRepositoryVo that = (GithubRepositoryVo) o;
        return Objects.equals(m_id, that.m_id) &&
            Objects.equals(m_fullName, that.m_fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_id, m_fullName);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GithubRepositoryVo{");
        sb.append("m_id='").append(m_id).append('\'');
        sb.append(", m_fullName='").append(m_fullName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
