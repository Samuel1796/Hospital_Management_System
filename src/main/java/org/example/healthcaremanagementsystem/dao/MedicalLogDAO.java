package org.example.healthcaremanagementsystem.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.example.healthcaremanagementsystem.config.MongoDBConnection;
import org.example.healthcaremanagementsystem.model.MedicalLog;

import java.util.ArrayList;
import java.util.List;

public class MedicalLogDAO {
    private MongoCollection<Document> collection;

    public MedicalLogDAO() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        if (db != null) {
            this.collection = db.getCollection("medical_logs");
        }
    }

    public void addLog(MedicalLog log) {
        if (collection != null) {
            collection.insertOne(log.toDocument());
        }
    }

    public List<MedicalLog> getLogsByPatientId(int patientId) {
        List<MedicalLog> logs = new ArrayList<>();
        if (collection != null) {
            FindIterable<Document> iter = collection.find(Filters.eq("patientId", patientId))
                    .sort(new Document("timestamp", -1));
            try (MongoCursor<Document> cursor = iter.iterator()) {
                while (cursor.hasNext()) {
                    logs.add(new MedicalLog(cursor.next()));
                }
            }
        }
        return logs;
    }

    public List<MedicalLog> getLogsByDoctorId(int doctorId) {
        List<MedicalLog> logs = new ArrayList<>();
        if (collection != null) {
            FindIterable<Document> iter = collection.find(Filters.eq("doctorId", doctorId))
                    .sort(new Document("timestamp", -1));
            try (MongoCursor<Document> cursor = iter.iterator()) {
                while (cursor.hasNext()) {
                    logs.add(new MedicalLog(cursor.next()));
                }
            }
        }
        return logs;
    }
}
