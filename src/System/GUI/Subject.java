/*
 * Copyright 2019 Milan Kablar, Hayden Klein, Melissa Lin, James Lang, Jack Haek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Course: SE2030 - 031
 * Fall 2019
 * Lab 9 - Implementation lab
 * Name: Group H (Hayden Klein, Milan Kablar, Jack Haek, Melissa Lin, James Lang)
 * Created: 10/13/2019
 */
package System.GUI;


import System.GUI.Observer;

/**
 * @author langjr
 * @version 1.0
 * @created 10-Oct-2019 1:41:14 PM
 */
public interface Subject {

	/**
	 * Abstract method which adds observer to the observers list
	 * @param obj object that is observer
	 */
	void register(Observer obj);

	/**
	 * Abstract method which removes observer from the observers list
	 * @param obj object that is observer
	 */
	void unregister(Observer obj);

	/**
	 * Abstract method which notifies observers of update
	 */
	void notifyObservers();


}