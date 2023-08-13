package com.smallworld;

import com.smallworld.data.Transaction;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionDataFetcher {

	public static void main(String[] args) {

		TransactionDataFetcher tdf = new TransactionDataFetcher("transactions.json");
		
		///TEST CASES:

		// System.out.println(tdf.getTotalTransactionAmount());

		// System.out.println(tdf.getTotalTransactionAmountSentBy("Tom Shelby"));

		// System.out.println(tdf.getMaxTransactionAmount());

		// System.out.println(tdf.countUniqueClients());

		// System.out.println(tdf.hasOpenComplianceIssues("Aberama Gold"));

		// System.out.println(tdf.getTransactionsByBeneficiaryName());

		// System.out.println(tdf.getUnsolvedIssueIds());

		// System.out.println(tdf.getAllSolvedIssueMessages());

		//// List<Transaction> list = tdf.getTop3TransactionsByAmount();
		// System.out.println(tdf.getTop3TransactionsByAmount());

		//Optional<String> strSender = tdf.getTopSender();
		//System.out.println(strSender.isPresent() ? strSender.get() : "");

		//Map<String, Transaction> map = tdf.getTransactionsByBeneficiaryName();
		//System.out.println(map);

	}

	private List<Transaction> transactions;

	public TransactionDataFetcher(String jsonFileName) {

		try {
			loadTransactionsFromJson(jsonFileName);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	private void loadTransactionsFromJson(String jsonFileName) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String strJSON = new String(Files.readAllBytes(Paths.get(jsonFileName)));

		transactions = objectMapper.readValue(strJSON, new TypeReference<List<Transaction>>() {
		});
	}

	/**
	 * Returns the sum of the amounts of all transactions
	 */
	public double getTotalTransactionAmount() {
		return transactions.stream()
				.collect(Collectors.toMap(Transaction::getMtn, Transaction::getAmount,
						(existingAmount, newAmount) -> existingAmount))
				.values().stream().mapToDouble(Double::doubleValue).sum();
	}

	/**
	 * Returns the sum of the amounts of all transactions sent by the specified
	 * client
	 */
	public double getTotalTransactionAmountSentBy(String senderFullName) {
		return transactions.stream().filter(transaction -> transaction.getSenderFullName().equals(senderFullName))
				.collect(Collectors.toMap(Transaction::getMtn, Transaction::getAmount,
						(existingAmount, newAmount) -> existingAmount))
				.values().stream().mapToDouble(Double::doubleValue).sum();
	}

	/**
	 * Returns the highest transaction amount
	 */
	public double getMaxTransactionAmount() {
		return transactions.stream().mapToDouble(Transaction::getAmount).max().getAsDouble();
	}

	/**
	 * Counts the number of unique clients that sent or received a transaction
	 */
	public long countUniqueClients() {
		Set<String> uniqueClients = transactions.stream().map(Transaction::getSenderFullName).distinct()
				.collect(Collectors.toSet());
		uniqueClients.addAll(
				transactions.stream().map(Transaction::getBeneficiaryFullName).distinct().collect(Collectors.toSet()));

		return uniqueClients.size();
	}

	/**
	 * Returns whether a client (sender or beneficiary) has at least one transaction
	 * with a compliance issue that has not been solved
	 */
	public boolean hasOpenComplianceIssues(String clientFullName) {
		return transactions.stream()
				.anyMatch(transaction -> (transaction.getSenderFullName().equals(clientFullName)
						|| transaction.getBeneficiaryFullName().equals(clientFullName))
						&& !transaction.getIssueSolved());
	}

	/**
	 * Returns all transactions indexed by beneficiary name
	 */
	public Map<String, Transaction> getTransactionsByBeneficiaryName() {
		return transactions.stream().distinct()
				.collect(Collectors.toMap(Transaction::getBeneficiaryFullName, transaction -> transaction));
	}

	/**
	 * Returns the identifiers of all open compliance issues
	 */
	public Set<Integer> getUnsolvedIssueIds() {
		return transactions.stream().filter(transaction -> !transaction.getIssueSolved()).map(Transaction::getIssueId)
				.distinct().collect(Collectors.toSet());
	}

	/**
	 * Returns a list of all solved issue messages
	 */
	public List<String> getAllSolvedIssueMessages() {
		return transactions.stream()
				.filter(transaction -> transaction.getIssueSolved() && !transaction.getIssueMessage().isBlank())
				.map(Transaction::getIssueMessage).distinct().collect(Collectors.toList());
	}

	/**
	 * Returns the 3 transactions with the highest amount sorted by amount
	 * descending
	 */
	public List<Transaction> getTop3TransactionsByAmount() {
		return transactions.stream().distinct().sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
				.limit(3).collect(Collectors.toList());
	}

	/**
	 * Returns the senderFullName of the sender with the most total sent amount
	 */
	public Optional<String> getTopSender() {
		return transactions.stream().distinct()
				.collect(Collectors.groupingBy(Transaction::getSenderFullName,
						Collectors.summingDouble(Transaction::getAmount)))
				.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
	}

}
