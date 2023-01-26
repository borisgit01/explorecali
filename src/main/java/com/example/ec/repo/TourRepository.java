package com.example.ec.repo;

import com.example.ec.domain.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Tour Repository Interface
 *
 * Created by Mary Ellen Bowman
 */
//public interface TourRepository extends CrudRepository<Tour, Integer> {
public interface TourRepository extends PagingAndSortingRepository<Tour, Integer> {
    //List<Tour> findByTourPackageCode(@Param("code")String code);
    Page<Tour> findByTourPackageCode(@Param("code")String code, Pageable pageable);
}
