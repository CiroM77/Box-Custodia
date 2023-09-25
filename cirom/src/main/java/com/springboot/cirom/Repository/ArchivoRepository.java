/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.springboot.cirom.Repository;

import com.springboot.cirom.model.Archivo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Long>{
    Optional<Archivo> findByHashSha256(String hashSha256);
    Optional<Archivo> findByHashSha512(String hashSha512);
    List<Archivo> findAll();
    Optional<Archivo> findByFileName(String fileName);
}
