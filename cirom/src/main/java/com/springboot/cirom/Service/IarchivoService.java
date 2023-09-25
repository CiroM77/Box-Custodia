/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.springboot.cirom.Service;

import com.springboot.cirom.model.Archivo;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ciro-
 */
public interface IarchivoService {
    public ResponseEntity<Map<String, Object>> cargarArchivo(MultipartFile archivo, String hashAlgoritmo) throws IOException, NoSuchAlgorithmException;
    public List<Map<String, Object>> obtenerArchivos();
    public Archivo buscarArchivo(String hashType, String hash);
}
