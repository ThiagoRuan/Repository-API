package br.ufpb.dcx.dsc.repositorios.services;

import br.ufpb.dcx.dsc.repositorios.exception.NotFoundException;
import br.ufpb.dcx.dsc.repositorios.models.Photo;
import br.ufpb.dcx.dsc.repositorios.models.Organizacao;
import br.ufpb.dcx.dsc.repositorios.models.User;
import br.ufpb.dcx.dsc.repositorios.repository.OrganizacaoRepository;
import br.ufpb.dcx.dsc.repositorios.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizacaoService {
    private OrganizacaoRepository organizacaoRepository;
    private UserRepository userRepository;
    public OrganizacaoService( UserRepository userRepository, OrganizacaoRepository organizacaoRepository){
        this.organizacaoRepository = organizacaoRepository;
        this.userRepository = userRepository;
    }

    public List<Organizacao> listOrganizacoes() {
        return organizacaoRepository.findAll();
    }
    public Organizacao getOrganizacao(Long orgId) {
        if(orgId == null) {
            throw new IllegalArgumentException("O ID da Organização não pode ser nulo.");
        }
        return organizacaoRepository.findById(orgId).orElseThrow(() -> new NotFoundException("Organização "+orgId+" não encontrada."));
    }

    public Organizacao createOrganizacao(Organizacao org, Long userId){
        Optional<User> uOpt = userRepository.findById(userId);
        if(uOpt.isPresent()) {
            Organizacao o = organizacaoRepository.save(org);
            User u = uOpt.get();
            u.getOrganizacaos().add(org);
            userRepository.save(u);
            return o;
        }
        throw new NotFoundException("Usuário "+userId+" não encontrado.");
    }

    public Organizacao updateOrganizacao(Long orgId, Organizacao o) {
        Optional<Organizacao> orgOpt = organizacaoRepository.findById(orgId);
        if(orgOpt.isPresent()) {
            Organizacao org = orgOpt.get();
            org.setNome(o.getNome());
            return organizacaoRepository.save(org);
        }
        throw new NotFoundException("Organização "+orgId+" não encontrada.");
    }

    public void deleteOrganizacao(Long orgId) {
        Optional<Organizacao> oOpt = organizacaoRepository.findById(orgId);
        if(oOpt.isPresent()) {
            Organizacao o = oOpt.get();

            o.getUsers().stream().forEach( user -> {
                user.getOrganizacaos().remove(o);
                userRepository.save(user);
            });
            o.getUsers().removeAll(o.getUsers());
            organizacaoRepository.delete(o);
        }
        throw new NotFoundException("Organização "+orgId+" não encontrada.");
    }
}
