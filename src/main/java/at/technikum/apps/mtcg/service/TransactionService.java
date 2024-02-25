package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.Repository;
import at.technikum.apps.mtcg.repository.TransactionRepository;

public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService() {
        this.transactionRepository = new TransactionRepository();
    }

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void acquirePackages(String username) {
        transactionRepository.acquirePackages(username);
    }
}
