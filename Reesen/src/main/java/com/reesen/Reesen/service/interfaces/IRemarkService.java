package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Remark;
import com.reesen.Reesen.model.User;

import java.util.List;

public interface IRemarkService {

    Remark save(Remark remark);
    Remark findOne(Long id);
    List<Remark> getRemarksByUser(User user);
}
