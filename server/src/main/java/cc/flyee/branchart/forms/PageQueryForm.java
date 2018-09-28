package cc.flyee.branchart.forms;

import cc.flyee.branchart.models.PageModel;

public class PageQueryForm {

    private Integer pageNumber=0;
    private Integer pageSize=20;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public PageModel getPager(){
        return new PageModel(pageNumber, pageSize);
    }
}
