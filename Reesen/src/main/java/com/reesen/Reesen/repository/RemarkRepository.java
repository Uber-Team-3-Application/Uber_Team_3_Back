package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Remark;
import com.reesen.Reesen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RemarkRepository extends JpaRepository<Remark, Long> {
    public List<Remark> getRemarksByUser(User user);
}
