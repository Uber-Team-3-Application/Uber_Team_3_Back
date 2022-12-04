package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Remark;
import com.reesen.Reesen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Set;

public interface RemarkRepository extends JpaRepository<Remark, Long> {
    public Set<Remark> getRemarksByUser(User user);
}
