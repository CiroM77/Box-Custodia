/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.cirom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Archivo {
@Id
@GeneratedValue(strategy=GenerationType.AUTO)

    private Long id;
    private String fileName;
    private String hashSha256;
    private String hashSha512;
    private LocalDateTime lastUpload;
    
   public Archivo(){
   }
   
   public Archivo(long id, String fileName, String hashSha256, String hashSha512,LocalDateTime lastUpload){
       
       this.id = id;
       this.fileName = fileName;
       this.hashSha256 = hashSha256;
       this.hashSha512 = hashSha512;
       this.lastUpload = lastUpload;
   
   }

}
