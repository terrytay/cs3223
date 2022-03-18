package simpledb.demo;

import simpledb.plan.Planner;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class CreateDB {
	public static void main(String[] args) {

	      try {
	         SimpleDB db = new SimpleDB("demodb");
	         Transaction tx = db.newTx();
	         Planner planner = db.planner();
	         
//	         
	         String s = "create table MODULES( MCode varchar(10), MName varchar(50), MCap int, MFacultyID int, MC int)";
	         planner.executeUpdate(s, tx);
	         
	         System.out.println("Table MODULES created.");
	         
	         
	         s = "create index mfacultyid on MODULES(MFacultyID) using btree";
	         planner.executeUpdate(s, tx);
	         
	         System.out.println("[MODULES] INDEX created on MFacultyID");
	         


	         s = "insert into MODULES(MCode, MName, MCap, MFacultyID, MC) values ";
	         String[] vals = {"('CS4246', 'AI Planning and Decision Making', 43, 1, 4)",
	        		 "('CS4225', 'Big Data Systems', 20, 1, 4)",
	        		 "('CS3235', 'Computer Security', 30, 1, 4)",
	        		 "('CS3223', 'Database System Implementation', 10, 1, 4)",
	        		 "('CS3234', 'Intro To Artificial Intelligence', 20, 1, 4)",
	        		 "('CS5242', 'Neural Networks and Deep Learning', 41, 1, 4)",
	        		 "('CN6020', 'Advanced Reaction Engineering', 40, 2, 4)",
	        		 "('CG4002', 'Capstone Project', 50, 2, 8)",
	        		 "('EE4210', 'Computer Networks', 35, 2, 4)",
	        		 "('EE4218', 'Embedded Hardware Systems Design', 30, 2, 4)",
	        		 "('ME5608B', 'Hybrid Manufacturing', 25, 2, 2)",
	        		 "('CE5203', 'Traffic Flow & Control', 63, 2, 4)",
	        		 "('CSA6102', 'Cultural Studies in Asia', 25, 3, 4)",
	        		 "('CH4207', 'History Of Chinese Language', 35, 3, 5)",
	        		 "('AH2101', 'Intro to Art History', 60, 3, 4)",
	        		 "('EC1101E', 'Intro to Economic Analysis', 30, 3, 4)",
	        		 "('AC5001', 'Architectural History of Singapore', 10, 4, 4)",
	        		 "('AR2224', 'Ideas and Approaches in Design', 35, 4, 4)",
	        		 "('MLE4208', 'Photovoltaics Materials', 53, 4, 4)",
	        		 "('CN3135', 'Process Safety, Health and Environment', 65, 4, 3)",
	        		 "('CE5104B', 'Tunnelling in Rocks', 36, 4, 2)",
	        		 "('BL5220', 'Advanced Animal Development', 62, 5, 4)",
	        		 "('FST3103', 'Advanced Food Engineering', 31, 5, 4)",
	        		 "('HS1501', 'Artificial Intelligence and Society', 50, 5, 4)",
	        		 "('BL5210', 'Biogeography', 64, 5, 2)",
	        		 "('PR5130', 'Advanced Pharmacotherapy I', 49, 5, 2)",
	        		 "('ACC1701', 'Accounting for Decision Makers', 84, 6, 4)",
	        		 "('MKT2711', 'Marketing Venture Challenge', 14, 6, 4)",
	        		 "('RE5018', 'Statutory Valuation', 61, 6, 4)",
	        		 "('DI5100', 'Dental Implantology', 6, 7, 1)",
	        		 "('GMS5003', 'Fundamentals of Health Products Regulation', 10, 8, 4)",
	        		 "('GMS5005', 'Regulation of Advanced Therapies', 5, 8, 4)",
	        		 "('GMS1000', 'The Duke-NUS Premed Course', 8, 8, 4)",
	        		 "('LL5008BV', 'Charterparties', 26, 9, 5)",
	        		 "('LL4093', 'Chinese Intellectual Property Law', 35, 9, 4)",
	        		 "('LC2013', 'Corporate Deals', 53, 9, 4)",
	        		 "('MDG5249', 'Ageing and Ethics', 26, 10, 4)",
	        		 "('MDG5243', 'Biology of Disease', 32, 10, 4)",
	        		 "('MUA1157', 'Solfege 1', 13, 11, 2)",
	        		 "('MUA3163', 'Musical Pathways', 81, 11, 4)",
	        		 "('HSS1000', 'Understanding Social Complexity', 60, 12, 4)",
	        		 "('QT5201M', 'Quantum State Estimation', 58, 13, 4)",
	        		 "('FT5101', 'FinTech Research Immersion', 32, 13, 4)",
	        		 "('SPH5414', 'Informatics for Health', 49, 14, 4)",
	        		 "('PP5028', 'Food System Resilience', 42, 15, 1)",
	        		 "('PP5409', 'Foundations of Public Policy', 52, 15, 4)",
	        		 "('TIC2601', 'Database and Web Applications', 45, 16, 4)",
	        		 "('USR4002A', 'Critical Reflection', 45, 17, 4)",
	        		 "('USE2325', 'Democracy and Inequality', 86, 17, 4)",
	        		 "('UAR2207', 'Reinventing Intercultural Exchanges', 23, 17, 4)"};
	         
	         
	         for (int i=0; i<vals.length; i++)
	            planner.executeUpdate(s + vals[i], tx);
	         
	         System.out.println("MODULES records inserted.");
	         
	         tx.commit();
	         tx = db.newTx();
	         planner = db.planner();


	         s = "create table STUDENTS( SID int, SName varchar(50), MajorID int, GradYr int)";
	         planner.executeUpdate(s, tx);
	         
	         System.out.println("Table STUDENTS created.");
	         
	         s = "create index majorid on STUDENTS(MajorID) using btree";
	         planner.executeUpdate(s, tx);
	         
//	         System.out.println("[STUDENTS] INDEX created on MajID");
	         
//	         s = "create index sid on STUDENTS(SID) using hash";
//	         planner.executeUpdate(s, tx);
//	         
//	         System.out.println("[STUDENTS] INDEX created on SID");
	         
	         
	         s = "insert into STUDENTS(SID, SName, MajorID, GradYr) values ";
	         String[] studvals = {"(1, 'joe', 10, 2021)",
	               "(2, 'amy', 21, 2020)",
	               "(3, 'max', 10, 2022)",
	               "(4, 'sue', 22, 2022)",
	               "(5, 'bob', 22, 2020)",
	               "(6, 'kim', 30, 2020)",
	               "(7, 'art', 11, 2021)",
	               "(8, 'pat', 2, 2019)",
	               "(9, 'lee', 1, 2021)",
	               "(10, 'terry', 4, 2022)",
	               "(11, 'joey', 3, 2022)",
	               "(12, 'anna', 3, 2020)",
	               "(13, 'manuel', 4, 2022)",
	               "(14, 'nizar', 6, 2022)",
	               "(15, 'sanath', 8, 2020)",
	               "(16, 'eliz', 28, 2023)",
	               "(17, 'maxiao', 9, 2021)",
	               "(18, 'cheryl', 20, 2019)",
	               "(19, 'belle', 10, 2021)",
	               "(20, 'tinzar', 13, 2021)",
	               "(21, 'yuheng', 10, 2023)",
	               "(22, 'nazri', 24, 2020)",
	               "(23, 'bosco', 23, 2024)",
	               "(24, 'zeon', 45, 2022)",
	               "(25, 'zihao', 44, 2023)",
	               "(26, 'kimmy', 44, 2022)",
	               "(27, 'jean', 41, 2024)",
	               "(28, 'chengyao', 42, 2019)",
	               "(29, 'shunquan', 45, 2022)",
	               "(30, 'kumar', 50, 2022)",
	               "(31, 'ravi', 51, 2021)",
	               "(32, 'chen', 53, 2020)",
	               "(33, 'kun', 50, 2022)",
	               "(34, 'xiao', 56, 2021)",
	               "(35, 'john', 57, 2020)",
	               "(36, 'yuhao', 59, 2020)",
	               "(37, 'seb', 30, 2021)",
	               "(38, 'tyler', 20, 2021)",
	               "(39, 'zen', 10, 2021)",
	               "(40, 'james', 10, 2021)",
	               "(41, 'saheera', 10, 2021)",
	               "(42, 'hussain', 20, 2020)",
	               "(43, 'jithin', 10, 2022)",
	               "(44, 'raju', 20, 2023)",
	               "(45, 'sanghit', 30, 2024)",
	               "(46, 'bala', 20, 2020)",
	               "(47, 'patty', 30, 2021)",
	               "(48, 'selena', 20, 2021)",
	               "(49, 'luoyi', 10, 2021)",
	               "(50, 'franco', 10, 2021)"};
	         for (int i=0; i<studvals.length; i++) {
	            planner.executeUpdate(s + studvals[i], tx);
	         }
	         
	         System.out.println("STUDENTS records inserted."); 
	         
	         tx.commit();
	         tx = db.newTx();
	         planner = db.planner();
	         
	         
	         s = "create table MAJORS(MajID int, MajName varchar(50), FacultyID int)";
	         planner.executeUpdate(s, tx);
	         
	         System.out.println("Table MAJORS created.");
//	         
	         s = "create index facultyid on MAJORS(FacultyID) using btree";
	         planner.executeUpdate(s, tx);
//	         
//	         System.out.println("[MAJORS] INDEX created on MajID");
	      
	         s = "insert into MAJORS(MajID, MajName, FacultyID) values ";
	         String[] majvals = { "(1, 'Chinese Language', 3)",
	         "(2, 'Chinese Studies', 3)",
	         "(3, 'Japanese Studies', 3)",
	         "(4, 'Malay Studies', 3)",
	         "(5, 'South Asian Studies', 3)",
	         "(6, 'Southeast Asian Studies', 3)",
	         "(7, 'English Language and Linguistics', 3)",
	         "(8, 'English Literature', 3)",
	         "(9, 'History', 3)",
	         "(10, 'Philosophy', 3)",
	         "(11, 'Theater and Performance Studies', 3)",
	         "(12, 'Anthropology', 3)",
	         "(13, 'Communication and New Media', 3)",
	         "(14, 'Economics', 3)",
	         "(15, 'Geography', 3)",
	         "(16, 'Political Science', 3)",
	         "(17, 'Social Work', 3)",
	         "(18, 'Sociology', 3)",
	         "(19, 'Accountancy', 6)",
	         "(20, 'Business Administration', 6)",
	         "(21, 'Business Analytics', 1)",
	         "(22, 'Computer Science', 1)",
	         "(23, 'Information Security', 1)",
	         "(24, 'Information Systems', 1)",
	         "(25, 'Computing Engineering', 1)",
	         "(25, 'Computing Engineering', 2)",
	         "(26, 'BTech Computing', 16)",
	         "(27, 'BTech Engineering', 16)",
	         "(28, 'Dentistry', 7)",
	         "(29, 'Architecture', 4)",
	         "(30, 'Industrial Design', 4)",
	         "(31, 'Landscape Architecture', 4)",
	         "(32, 'Project & Facilities Management', 4)",
	         "(33, 'Real Estate', 4)",
	         "(34, 'Biomedical Engineering', 2)",
	         "(35, 'Chemical Engineering', 2)",
	         "(36, 'Civil Engineering', 2)",
	         "(37, 'Electrical Engineering', 2)",
	         "(38, 'Engineering Science', 2)",
	         "(39, 'Environmental Engineering', 2)",
	         "(40, 'Industrial and Systems Engineering', 2)",
	         "(41, 'Materials Science and Engineering', 2)",
	         "(42, 'Mechanical Engineering', 2)",
	         "(43, 'Medicine', 8)",
	         "(44, 'Law', 9)",
	         "(45, 'Medicine', 10)",
	         "(46, 'Nursing', 10)",
	         "(47, 'Music', 11)",
	         "(48, 'Design and Engineering', 12)",
	         "(49, 'Humanities and Sciences', 12)",
	         "(50, 'Integrative Sciences and Engineering Programme', 13)",
	         "(51, 'Public Health', 14)",
	         "(52, 'Internal Affairs', 15)",
	         "(53, 'Public Policy', 15)",
	         "(54, 'Public Administration', 15)",
	         "(55, 'Public Administration and Management', 15)",
	         "(56, 'Environmental Studies', 5)",
	         "(57, 'Food Science and Technology', 5)",
	         "(58, 'Humanities and Sciences', 5)",
	         "(59, 'Pharmaceutical Science', 5)",
	         "(60, 'Pharmacy', 5)",
	         "(61, 'Philosophy, Politics, and Economics', 5)",
	         "(62, 'Anthropology', 18)",
	         "(63, 'Arts and Humanities', 18)",
	         "(64, 'Economics', 18)",
	         "(65, 'Environmental Studies', 18)",
	         "(66, 'Global Affairs', 18)",
	         "(67, 'History', 18)",
	         "(68, 'Life Sciences', 18)",
	         "(69, 'Literature', 18)",
	         "(70, 'Mathematical, Computational & Statistical Sciences', 18)",
	         "(71, 'Philosophy', 18)",
	         "(72, 'Philosophy, Politics and Economics', 18)",
	         "(73, 'Physical Sciences', 18)",
	         "(74, 'Psychology', 18)",
	         "(75, 'Urban Studies', 18)",
	         "(76, 'Chinese Studies', 18)",
	         "(77, 'Global Antiquity', 18)"};
	         
	         
	         for (int i=0; i<majvals.length; i++)
		            planner.executeUpdate(s + majvals[i], tx);
	         
	         System.out.println("MAJORS records inserted."); 
	         
	         tx.commit();
	         tx = db.newTx();
	         planner = db.planner();
	         
	         s = "create table FACULTY(FID int, FName varchar(50))";
	         planner.executeUpdate(s, tx);
	         
	         System.out.println("Table FACULTY created.");
	         
	         s = "insert into FACULTY(FID, FName) values ";
	         String[] fvals = {"(1, 'Computing')",
	        		 "(2, 'Engineering')",
	        		 "(3, 'Arts & Social Sciences')",
	        		 "(4, 'Design & Environment')",
	        		 "(5, 'Science')",
	        		 "(6, 'Business')",
	        		 "(7, 'Dentistry')",
	        		 "(8, 'Duke-NUS')",
	        		 "(9, 'Law')",
	        		 "(10, 'Medicine')",
	        		 "(11, 'Music')",
	        		 "(12, 'NUS College')",
	        		 "(13, 'NUS Graduate School')",
	        		 "(14, 'Public Health')",
	        		 "(15, 'Public Policy')",
	        		 "(16, 'Continuing and Lifelong Education')",
	        		 "(17, 'University Scholars Programme')",
	        		 "(18, 'Yale-NUS')"};
	         for (int i=0; i<fvals.length; i++)
		            planner.executeUpdate(s + fvals[i], tx);
	         
	         System.out.println("FACULTY records inserted."); 
	         tx.commit();
	      }
	      catch(Exception e) {
	         e.printStackTrace();
	      }
	   }
}
