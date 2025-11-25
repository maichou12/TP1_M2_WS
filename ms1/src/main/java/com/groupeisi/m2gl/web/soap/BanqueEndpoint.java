package com.groupeisi.m2gl.web.soap;

import com.groupeisi.m2gl.entities.GetRoleRequest;
import com.groupeisi.m2gl.entities.GetSoldeRequest;
import com.groupeisi.m2gl.entities.RoleResponse;
import com.groupeisi.m2gl.entities.SoldeResponse;
import com.groupeisi.m2gl.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * Endpoint SOAP pour les services bancaires.
 */
@Endpoint
public class BanqueEndpoint {

    private static final String NAMESPACE_URI = "http://www.isi.com/banque";

    private final ClientService clientService;

    @Autowired
    public BanqueEndpoint(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Méthode pour récupérer le solde d'un client.
     *
     * @param request la requête contenant le numéro de téléphone
     * @return la réponse contenant le solde
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSoldeRequest")
    @ResponsePayload
    public SoldeResponse getSolde(@RequestPayload GetSoldeRequest request) {
        SoldeResponse response = new SoldeResponse();
        Long solde = clientService.getSolde(request.getTel());
        response.setSolde(solde);
        return response;
    }

    /**
     * Méthode pour récupérer le rôle d'un client.
     *
     * @param request la requête contenant le numéro de téléphone
     * @return la réponse contenant le rôle
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getRoleRequest")
    @ResponsePayload
    public RoleResponse getRole(@RequestPayload GetRoleRequest request) {
        RoleResponse response = new RoleResponse();
        String role = clientService.getRole(request.getTel());
        response.setRole(role);
        return response;
    }
}
