package com.example.ec.repo;

import com.example.ec.domain.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

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

    @Override
    @RestResource(exported = false)
    Iterable<Tour> findAll(Sort sort);

    @Override
    @RestResource(exported = false)
    <S extends Tour> S save(S s);

    @Override
    @RestResource(exported = false)
    <S extends Tour> Iterable<S> saveAll(Iterable<S> iterable);

    @Override
    @RestResource(exported = false)
    void deleteById(Integer integer);

    @Override
    @RestResource(exported = false)
    void delete(Tour tour);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Tour> iterable);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
