package br.ufpb.dcx.dsc.repositorios.services;

import br.ufpb.dcx.dsc.repositorios.exception.NotFoundException;
import br.ufpb.dcx.dsc.repositorios.models.Organizacao;
import br.ufpb.dcx.dsc.repositorios.models.Repositorio;
import br.ufpb.dcx.dsc.repositorios.repository.OrganizacaoRepository;
import br.ufpb.dcx.dsc.repositorios.repository.RepositorioRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RepositorioService {

    private RepositorioRepository repositorioRepository;
    private OrganizacaoRepository organizacaoRepository;
    RepositorioService(RepositorioRepository repositorioRepository, OrganizacaoRepository organizacaoRepository){
        this.repositorioRepository=repositorioRepository;
        this.organizacaoRepository = organizacaoRepository;
    }
    public Repositorio getRepositorio(Long id) {
        return repositorioRepository.findById(id).orElseThrow(() -> new NotFoundException("Repositório "+id+" não encontrado."));
    }

    public List<Repositorio> listRepositorios() {
        return repositorioRepository.findAll();
    }

    public Repositorio saveRepositorio(Repositorio r, Long orgId) {
        Optional<Organizacao> oOpt = organizacaoRepository.findById(orgId);
        if(oOpt.isPresent()) {
            r.setOrganizacao(oOpt.get());
            return repositorioRepository.save(r);
        }
        throw new NotFoundException("Organização "+orgId+" não encontrada.");

    }

    public void deleteRepositorio(Long id) {
        if(!repositorioRepository.existsById(id)) {
            throw new NotFoundException("Repositório "+id+" não encontrado.");
        }
    }

    public Repositorio updateRepositorio(Long id, Repositorio rUpdated) {
        Optional<Repositorio> r = repositorioRepository.findById(id);
        if(r.isPresent()) {
            Repositorio toUpdate = r.get();
            toUpdate.setIsPrivate(rUpdated.getIsPrivate());
            toUpdate.setNome(rUpdated.getNome());
            return repositorioRepository.save(toUpdate);
        }
        throw new NotFoundException("Repositório "+id+" não encontrado.");
    }
}
