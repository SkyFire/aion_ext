namespace Jamie.ParserBase
{
	using System;
	using System.Collections.Generic;
	using System.Linq;
	using System.Reflection;
	using System.Text.RegularExpressions;

	public interface IDynamicImport<T> where T : class, new()
	{
		void Import(T importObject, IEnumerable<FieldInfo> getters);
	}

	public interface IAdvice
	{
		Type GetClassType(FieldInfo getter);
	}

	static class TypeExtensions
	{
		static Regex fieldNamePattern = new Regex(@"^(?<name>[^\d]*)\d+", RegexOptions.Compiled);

		public const BindingFlags Flags = BindingFlags.Public | BindingFlags.Instance |
										  BindingFlags.ExactBinding | BindingFlags.GetField;

		public static IEnumerable<string> GetStartNames(this Type type) {
			foreach (var fi in type.GetFields(Flags)) {
				if (fieldNamePattern.IsMatch(fi.Name)) {
					Match match = fieldNamePattern.Match(fi.Name);
					var group = match.Groups[1];
					yield return group.Value;
				}
			}
		}
	}

	public class Utility<U> where U : class, new()
	{
		static Dictionary<string, List<FieldInfo>> getters;
		static Dictionary<string, FieldInfo> allGetters;

		static Utility<U> utilitySingleton = new Utility<U>();
		static bool initialized = false;

		private Utility() { }

		static Utility() {
			getters = new Dictionary<string, List<FieldInfo>>();
			allGetters = new Dictionary<string, FieldInfo>();
		}

		public static Utility<U> Instance {
			get {
				if (!initialized) {
					Initialize();
					initialized = true;
				}
				return utilitySingleton;
			}
		}

		static void Initialize() {
			var uniqueNames = typeof(U).GetStartNames().Distinct();
			foreach (string key in uniqueNames)
				AddGetters(key);
			foreach (FieldInfo fi in typeof(U).GetFields())
				allGetters.Add(fi.Name, fi);
		}

        public void AddGetter(string name) {
            var fis = typeof(U).GetFields(TypeExtensions.Flags).Where(f => f.Name == name);
            getters.Add(name, fis.OrderBy(f => f.Name).ToList());
        }

		static void AddGetters(string name) {
			var fis = typeof(U).GetFields(TypeExtensions.Flags).Where(f => f.Name.StartsWith(name) &&
																		   f.Name[name.Length] != '_');
			getters.Add(name, fis.OrderBy(f => f.Name).ToList());
		}

		public void Export<T>(U objectFrom, string name, List<T> toList) {
			if (!getters.ContainsKey(name)) {
				if (allGetters.ContainsKey(name)) {
					object obj = allGetters[name].GetValue(objectFrom);
					if (obj != null) {
						InterfaceMapping iMap = default(InterfaceMapping);
						try {
							iMap = typeof(T).GetInterfaceMap(typeof(IDynamicImport<U>));
						} catch (ArgumentException) { }
						if (iMap.InterfaceMethods != null) {
							Export<T>(objectFrom, new List<FieldInfo>() { allGetters[name] }, toList);
							return;
						}
						T value;
						try {
							value = (T)obj;
						} catch {
							value = (T)Convert.ChangeType(obj, typeof(T));
						}
						toList.Add(value);
					}
				}
				return;
			}
			IEnumerable<FieldInfo> useGetters = getters[name];
			Export<T>(objectFrom, useGetters, toList);
		}

		int deep = 0;

		public void SetIndexDeep(int deep) {
			this.deep = deep;
		}

		public void Export<T>(U objectFrom, IEnumerable<FieldInfo> fieldGetters, List<T> toList) {
			deep++;
			if (Type.GetTypeCode(typeof(T)) == TypeCode.Object) {
				List<List<FieldInfo>> groupedGetters = new List<List<FieldInfo>>();
				int index = 0;
				bool create = toList.Count == 0;
				foreach (FieldInfo fi in fieldGetters) {
					int level = deep;
					foreach (char c in fi.Name) {
						if (Char.IsDigit(c)) {
							if (level > 1) {
								level--;
								continue;
							}
							int newIndex = Int32.Parse(c.ToString());
							if (newIndex != index) {
								if (create) {
									T instance = default(T);
									if (typeof(T).IsAbstract) {
										InterfaceMapping iMap = default(InterfaceMapping);
										try {
											iMap = typeof(U).GetInterfaceMap(typeof(IAdvice));
										} catch (ArgumentException) { }
										if (iMap.InterfaceMethods != null) {
											Type type = ((IAdvice)objectFrom).GetClassType(fi);
											if (type != null && type.IsSubclassOf(typeof(T)))
												instance = (T)Activator.CreateInstance(type);
										}
									} else {
										instance = Activator.CreateInstance<T>();
									}
									if (instance == null) {
										index++;
										break;
									}
									toList.Add(instance);
								}
								groupedGetters.Add(new List<FieldInfo> { fi });
								index++;
							} else {
								groupedGetters.Last().Add(fi);
							}
							break;
						}
					}
				}
				if (index == 0 && fieldGetters.Count() == 1) {
					T instance = Activator.CreateInstance<T>();
					groupedGetters.Add(new List<FieldInfo> { fieldGetters.First() });
					toList.Add(instance);
				}
				for (int i = 0; i < groupedGetters.Count; i++) {
					if (toList.Count - 1 < i)
						break;
					IDynamicImport<U> dynAdd = (IDynamicImport<U>)toList[i];
					dynAdd.Import(objectFrom, groupedGetters[i]);
				}
			} else {
				foreach (var getter in fieldGetters) {
					if (typeof(T).Equals(getter.FieldType)) {
						T item = (T)getter.GetValue(objectFrom);
						if (item != null)
							toList.Add(item);
					}
				}
			}
			deep--;
		}
	}
}
