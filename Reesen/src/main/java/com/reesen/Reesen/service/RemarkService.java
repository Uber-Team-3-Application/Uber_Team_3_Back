package com.reesen.Reesen.service;

import com.reesen.Reesen.model.Remark;
import com.reesen.Reesen.model.User;
import com.reesen.Reesen.repository.RemarkRepository;
import com.reesen.Reesen.service.interfaces.IRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RemarkService implements IRemarkService {

    private final RemarkRepository remarkRepository;

    @Autowired
    public RemarkService(RemarkRepository remarkRepository) {
        this.remarkRepository = remarkRepository;
    }

    @Override
    public Remark save(Remark remark) {
        return this.remarkRepository.save(remark);
    }

    @Override
    public Remark findOne(Long id) {
        return this.remarkRepository.findById(id).orElseGet(null);
    }

    @Override
    public Set<Remark> getRemarksByUser(User user) {
        return this.remarkRepository.getRemarksByUser(user);
    }


}
