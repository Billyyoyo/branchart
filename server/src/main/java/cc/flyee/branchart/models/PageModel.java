package cc.flyee.branchart.models;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageModel implements Serializable, Pageable {

    private static final long serialVersionUID = -1L;

    private Integer pageNumber;
    private Integer pageSize;

    private Sort sort = new Sort(Sort.Direction.DESC, "createTime");

    public PageModel(Integer pageNumber, Integer pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public PageModel(Integer pageNumber, Integer pageSize, Sort sort) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public long getOffset() {
        return (pageNumber - 1) * pageSize;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}
