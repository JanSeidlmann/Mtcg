package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.TransactionRepository;

public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService() {
        this.transactionRepository = new TransactionRepository();
    }

    public void acquirePackages(String username) {
        transactionRepository.acquirePackages(username);
    }
}
