package br.edu.ulbra.election.candidate.service;


import br.edu.ulbra.election.candidate.exception.GenericOutputException;

import br.edu.ulbra.election.candidate.input.v1.CandidateInput;

import br.edu.ulbra.election.candidate.model.Candidate;

import br.edu.ulbra.election.candidate.output.v1.GenericOutput;

import br.edu.ulbra.election.candidate.output.v1.CandidateOutput;

import br.edu.ulbra.election.candidate.repository.CandidateRepository;

import org.apache.commons.lang.StringUtils;

import org.modelmapper.ModelMapper;

import org.modelmapper.TypeToken;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.lang.reflect.Type;

import java.util.List;


@Service
public class CandidateService {

  
  private final CandidateRepository candidateRepository;

  
  private final ModelMapper modelMapper;

 
   

 
   private static final String MESSAGE_INVALID_ID = "Invalid id";
  
  private static final String MESSAGE_CANDIDATE_NOT_FOUND = "Candidate not found";

   
 @Autowired
    public CandidateService(CandidateRepository CandidateRepository, ModelMapper modelMapper){
      
  this.candidateRepository = candidateRepository;
  
      this.modelMapper = modelMapper;
   
     
  }

    public List<CandidateOutput> getAll(){
      
  Type candidateOutputListType = new TypeToken<List<CandidateOutput>>(){}.getType();
      
  return modelMapper.map(candidateRepository.findAll(), candidateOutputListType);
  

  }

    public CandidateOutput create(CandidateInput CandidateInput)
 {
        
validateInput(candidateInput, false);
  
      Candidate candidate= modelMapper.map(candidateInput, Candidate.class);
 
     
        candidate= candidateRepository.save(candidate);

        return modelMapper.map(candidate, CandidateOutput.class);
   
 }

    public CandidateOutput getById(Long candidateId){
      
  if (candidateId == null)
{
            throw new GenericOutputException(MESSAGE_INVALID_ID);
  
      }

   
     Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
  
      if (candidate == null){
         
   throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
   
     }

        return modelMapper.map(candidate, CandidateOutput.class);
 
   }

    public CandidateOutput update(Long candidateId, CandidateInput candidateInput) {
  
      if (candidateId == null){
        
    throw new GenericOutputException(MESSAGE_INVALID_ID);
    
    }
        validateInput(candidateInput, true);

     
   Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null){
  
          throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
        }

     
   candidate.setName(candidateInput.getName());
   
     candidate.setPartyId(candidateInput.getPartyId());
   
     candidate.setNumberElection(candidateInput.getNumberElection());
candidate.setElectionId(candidateInput.getElectionId());
          candidate = candidateRepository.save(candidate);
  
      return modelMapper.map(candidate, CandidateOutput.class);
   

 }

    public GenericOutput delete(Long candidateId) {
   
     if (candidateId == null){
 
           throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

     
   Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
  
      if (candidate == null){
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
 
       }

        candidateRepository.delete(candidate);

      
  return new GenericOutput("Candidate deleted");
  
  }

    private void validateInput(CandidateInput candidateInput, boolean isUpdate){
     
   if (StringUtils.isBlank(candidateInput.getName())){
            throw new GenericOutputException("Invalid name");
        }

   if (StringUtils.isBlank(candidateInput.getPartyId())){
            throw new GenericOutputException("Invalid Party");
    }


   if (StringUtils.isBlank(candidateInput.getNumberElection())){
            throw new GenericOutputException("Invalid Number Election");
    }
   if (StringUtils.isBlank(candidateInput.getElectionId())){
            throw new GenericOutputException("Invalid ElectionId");
    } 
   
                        