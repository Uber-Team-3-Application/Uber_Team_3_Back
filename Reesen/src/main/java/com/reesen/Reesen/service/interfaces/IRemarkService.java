package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Remark;
import com.reesen.Reesen.model.User;

import java.util.List;
import java.util.Set;

public interface IRemarkService {

    Remark save(Remark remark);
    Remark findOne(Long id);
    Set<Remark> getRemarksByUser(User user);
}
