package com.groupeisi.m2gl.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer les clients et leurs informations (solde, rôle).
 */
@Service
public class ClientService {

    // Map temporaire pour stocker les clients (numeroTel -> solde)
    private final Map<Long, Long> clientSoldes = new HashMap<>();

    // Map temporaire pour stocker les rôles des clients (numeroTel -> role)
    private final Map<Long, String> clientRoles = new HashMap<>();

    public ClientService() {
        // Initialisation avec quelques données de test
        clientSoldes.put(22112345678L, 50000L);
        clientSoldes.put(22112345679L, 100000L);
        clientSoldes.put(22112345680L, 25000L);

        clientRoles.put(22112345678L, "CLIENT");
        clientRoles.put(22112345679L, "VIP");
        clientRoles.put(22112345680L, "CLIENT");
    }

    /**
     * Récupère le solde d'un client par son numéro de téléphone.
     *
     * @param numeroTel le numéro de téléphone du client
     * @return le solde du client, ou 0 si le client n'existe pas
     */
    public Long getSolde(Long numeroTel) {
        return clientSoldes.getOrDefault(numeroTel, 0L);
    }

    /**
     * Récupère le rôle d'un client par son numéro de téléphone.
     *
     * @param numeroTel le numéro de téléphone du client
     * @return le rôle du client, ou "UNKNOWN" si le client n'existe pas
     */
    public String getRole(Long numeroTel) {
        return clientRoles.getOrDefault(numeroTel, "UNKNOWN");
    }

    /**
     * Vérifie si un client existe.
     *
     * @param numeroTel le numéro de téléphone du client
     * @return true si le client existe, false sinon
     */
    public boolean clientExists(Long numeroTel) {
        return clientSoldes.containsKey(numeroTel);
    }
}
