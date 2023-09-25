/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.cirom.Service;

import com.springboot.cirom.Repository.ArchivoRepository;
import com.springboot.cirom.model.Archivo;
import jakarta.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.springboot.cirom.Exception.ErrorException;
import java.time.format.DateTimeFormatter;


@Service
public class ArchivoService implements IarchivoService{
    
    @Autowired
    private ArchivoRepository archivoRepository;
    
    @Override
    public ResponseEntity<Map<String, Object>> cargarArchivo(MultipartFile archivo, String hashAlgoritmo) throws IOException, NoSuchAlgorithmException {
    
        // Leer el contenido del archivo
        byte[] contenido = archivo.getBytes();
               
        // Calcular el hash según el algoritmo especificado
        String hash;
        if ("SHA-256".equalsIgnoreCase(hashAlgoritmo)) {
            hash = calcularHash(contenido, "SHA-256");
        } else if ("SHA-512".equalsIgnoreCase(hashAlgoritmo)) {
            hash = calcularHash(contenido, "SHA-512");
        } else {
         throw new ErrorException("Algoritmo de Hash no válido"); 

        }       
        // Verificar si el archivo fue cargado previamente
        Optional<Archivo> archivoExistente = archivoRepository.findByHashSha256(hash);
        if (archivoExistente.isPresent()) {
            Archivo archivoDB = archivoExistente.get();
            archivoDB.setLastUpload(LocalDateTime.now());
            archivoRepository.save(archivoDB);
        } else {
            Archivo nuevoArchivo = new Archivo();
            nuevoArchivo.setFileName(archivo.getOriginalFilename());
            nuevoArchivo.setHashSha256(hash);
            nuevoArchivo.setHashSha512(calcularHash(contenido, "SHA-512"));
            nuevoArchivo.setLastUpload(LocalDateTime.now());
            archivoRepository.save(nuevoArchivo);
        }         
        // Formateo y solucion de la hora
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String formattedDateTime = LocalDateTime.now().format(formatter);
        
        // JSON de respuesta
        List<Archivo> archivos = archivoRepository.findAll();
        List<Map<String, Object>> documentos = new ArrayList<>();
        for (Archivo arch : archivos) {
        Map<String, Object> documento = new HashMap<>();
        documento.put("fileName", arch.getFileName());
        documento.put("hash", "SHA-256".equalsIgnoreCase(hashAlgoritmo) ? arch.getHashSha256() : arch.getHashSha512());
        if (arch.getLastUpload() != null) {
            documento.put("lastUpload", arch.getLastUpload());
        }
        documentos.add(documento);
    }
        Map<String, Object> respuestaJSON = new HashMap<>();
        respuestaJSON.put("algorithm", hashAlgoritmo);
        respuestaJSON.put("documents", documentos);
        return ResponseEntity.ok(respuestaJSON);

    }
    
     private String calcularHash(byte[] contenido, String algoritmo) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algoritmo);
        byte[] hashBytes = digest.digest(contenido);
        return DatatypeConverter.printHexBinary(hashBytes).toLowerCase();
    }
     
    @Override
     public List<Map<String, Object>> obtenerArchivos() {
        List<Archivo> archivos = archivoRepository.findAll();
        List<Map<String, Object>> archivosEnJSON = new ArrayList<>();

        for (Archivo archivo : archivos) {
            Map<String, Object> archivoJSON = new HashMap<>();
            archivoJSON.put("fileName", archivo.getFileName());
            archivoJSON.put("hashSha256", archivo.getHashSha256());
            archivoJSON.put("hashSha512", archivo.getHashSha512());
            archivoJSON.put("lastUpload", archivo.getLastUpload());
            archivosEnJSON.add(archivoJSON);
        }

        return archivosEnJSON;
    }
     
     public Archivo buscarArchivo(String hashType, String hash) {
        if ("SHA-256".equalsIgnoreCase(hashType)) {
            return archivoRepository.findByHashSha256(hash).orElse(null);
        } else if ("SHA-512".equalsIgnoreCase(hashType)) {
            return archivoRepository.findByHashSha512(hash).orElse(null);
        } else {
            // Manejar el caso en que el tipo de hash no es válido
            throw new ErrorException("El parámetro 'hash' solo puede ser ‘SHA-256’ o ‘SHA-512’");
    
        }
    }
    
    
}
