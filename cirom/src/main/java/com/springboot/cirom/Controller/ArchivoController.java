/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.cirom.Controller;

import com.springboot.cirom.Exception.ErrorException;
import com.springboot.cirom.Repository.ArchivoRepository;
import com.springboot.cirom.Service.IarchivoService;
import com.springboot.cirom.model.Archivo;
import jakarta.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ArchivoController {
    @Autowired
    private IarchivoService archivoService;
    
    @PostMapping("/documents/hash")
    public ResponseEntity<Map<String, Object>> cargarArchivo(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("hashAlgoritmo") String hashAlgoritmo) throws IOException, NoSuchAlgorithmException {
        
        ResponseEntity<Map<String, Object>> respuesta = archivoService.cargarArchivo(archivo, hashAlgoritmo);
        return respuesta;
    }
    
    @GetMapping("/documents")
    public ResponseEntity<List<Map<String, Object>>> obtenerArchivosEnJSON() {
        List<Map<String, Object>> archivosEnJSON = archivoService.obtenerArchivos();
        return ResponseEntity.ok(archivosEnJSON);
    }
    
    @GetMapping("/document")
    public ResponseEntity<Map<String, Object>> buscarArchivo(
            @RequestParam("hashType") String hashType,
            @RequestParam("hash") String hash) {
        
        // Llamo al servicio para buscar el archivo por su hash
        Archivo archivo = archivoService.buscarArchivo(hashType, hash);

        if (archivo != null) {
            // JSON de respuesta
            Map<String, Object> respuestaJSON = new HashMap<>();
            respuestaJSON.put("fileName", archivo.getFileName());
            respuestaJSON.put("hash", (hashType.equalsIgnoreCase("SHA-256")) ? archivo.getHashSha256() : archivo.getHashSha512());
            respuestaJSON.put("lastUpload", archivo.getLastUpload());

            return ResponseEntity.ok(respuestaJSON);
        } else {
            // Si no se encuentra el archivo, devolvemos el error
           throw new ErrorException("El archivo con el hash seleccionado no existe");
        }
    }
}
