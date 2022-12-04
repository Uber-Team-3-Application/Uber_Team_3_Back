package com.reesen.Reesen.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.reesen.Reesen.model.Path;
import com.reesen.Reesen.repository.PathRepository;
import com.reesen.Reesen.service.interfaces.IPathService;

public class PathSerivce implements IPathService {
	
	private final PathRepository pathRepository;
	
    @Autowired
    public PathSerivce(PathRepository pathRepository){
        this.pathRepository = pathRepository;
    }

	@Override
	public Path findOne(Long id) {
		return this.pathRepository.findById(id).orElseGet(null);
	}

	@Override
	public Path save(Path path) {
		return this.pathRepository.save(path);
	}

}
