package org.example.healthcaremanagementsystem.controller.pagination;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * Handles pagination logic for controllers.
 * Follows Single Responsibility Principle by managing only pagination state and controls.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class PaginationHandler {
    
    private int currentPage = 0;
    private int pageSize = 10;
    private int totalRecords = 0;
    private int totalPages = 0;
    
    private final Button btnFirstPage;
    private final Button btnPrevPage;
    private final Button btnNextPage;
    private final Button btnLastPage;
    private final Label lblPageInfo;
    private final Label lblRecordInfo;
    private final ComboBox<Integer> comboPageSize;
    
    /**
     * Constructor initializes pagination controls.
     * 
     * @param btnFirstPage First page button
     * @param btnPrevPage Previous page button
     * @param btnNextPage Next page button
     * @param btnLastPage Last page button
     * @param lblPageInfo Page info label
     * @param lblRecordInfo Record info label
     * @param comboPageSize Page size combo box
     */
    public PaginationHandler(Button btnFirstPage, Button btnPrevPage, 
                             Button btnNextPage, Button btnLastPage,
                             Label lblPageInfo, Label lblRecordInfo,
                             ComboBox<Integer> comboPageSize) {
        this.btnFirstPage = btnFirstPage;
        this.btnPrevPage = btnPrevPage;
        this.btnNextPage = btnNextPage;
        this.btnLastPage = btnLastPage;
        this.lblPageInfo = lblPageInfo;
        this.lblRecordInfo = lblRecordInfo;
        this.comboPageSize = comboPageSize;
        
        initializePageSizeCombo();
    }
    
    /**
     * Initializes page size combo box with options.
     */
    private void initializePageSizeCombo() {
        if (comboPageSize != null) {
            comboPageSize.getItems().addAll(5, 10, 20, 50, 100);
            comboPageSize.setValue(10);
            comboPageSize.setOnAction(e -> {
                if (comboPageSize.getValue() != null) {
                    setPageSize(comboPageSize.getValue());
                }
            });
        }
    }
    
    /**
     * Updates pagination controls based on current state.
     */
    public void updateControls() {
        totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (totalPages == 0) {
            totalPages = 1;
        }
        
        if (lblPageInfo != null) {
            lblPageInfo.setText("Page " + (currentPage + 1) + " of " + totalPages);
        }
        
        if (lblRecordInfo != null) {
            lblRecordInfo.setText(totalRecords + " records");
        }
        
        if (btnFirstPage != null) {
            btnFirstPage.setDisable(currentPage == 0);
        }
        if (btnPrevPage != null) {
            btnPrevPage.setDisable(currentPage == 0);
        }
        if (btnNextPage != null) {
            btnNextPage.setDisable(currentPage >= totalPages - 1);
        }
        if (btnLastPage != null) {
            btnLastPage.setDisable(currentPage >= totalPages - 1);
        }
    }
    
    /**
     * Goes to first page.
     */
    public void goToFirstPage() {
        if (currentPage > 0) {
            currentPage = 0;
        }
    }
    
    /**
     * Goes to previous page.
     */
    public void goToPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
        }
    }
    
    /**
     * Goes to next page.
     */
    public void goToNextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
        }
    }
    
    /**
     * Goes to last page.
     */
    public void goToLastPage() {
        if (currentPage < totalPages - 1) {
            currentPage = totalPages - 1;
        }
    }
    
    /**
     * Resets to first page.
     */
    public void reset() {
        currentPage = 0;
    }
    
    // Getters and Setters
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        reset();
    }
    
    public int getTotalRecords() {
        return totalRecords;
    }
    
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
        updateControls();
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public int getOffset() {
        return currentPage * pageSize;
    }
}

