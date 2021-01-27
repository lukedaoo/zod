package com.infamous.framework.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class GithubRepositoriesVo {


    @JsonProperty("total_count")
    String m_totalCount;

    @JsonProperty("items")
    List<GithubRepositoryVo> m_items;

    public GithubRepositoriesVo() {

    }

    public GithubRepositoriesVo(String totalCount, List<GithubRepositoryVo> items) {
        m_totalCount = totalCount;
        m_items = items;
    }

    public String getTotalCount() {
        return m_totalCount;
    }

    public GithubRepositoriesVo setTotalCount(String totalCount) {
        m_totalCount = totalCount;
        return this;
    }

    public List<GithubRepositoryVo> getItems() {
        return m_items;
    }

    public GithubRepositoriesVo setItems(List<GithubRepositoryVo> items) {
        m_items = items;
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
        GithubRepositoriesVo that = (GithubRepositoriesVo) o;
        return Objects.equals(m_totalCount, that.m_totalCount) &&
            Objects.equals(m_items, that.m_items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_totalCount, m_items);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GithubRepositoriesVo{");
        sb.append("m_totalCount='").append(m_totalCount).append('\'');
        sb.append(", m_items=").append(m_items);
        sb.append('}');
        return sb.toString();
    }
}
