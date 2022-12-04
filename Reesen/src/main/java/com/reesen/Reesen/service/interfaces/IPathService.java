package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Path;

public interface IPathService {
	Path findOne(Long id);
	Path save(Path path);
}
